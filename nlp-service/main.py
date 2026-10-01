import torch
import os
# PyTorch is a major ML framework/runtime, 
# not the universal backbone of all ML services.
os.environ["FLAGS_use_mkldnn"] = "0"
from fastapi import FastAPI, UploadFile, File, HTTPException
from pydantic import BaseModel
from transformers import pipeline
from paddleocr import PaddleOCR
from pathlib import Path

app = FastAPI(
    title="SignalBait NLP Service",
    version="1.0.0"
)

MODEL_NAME = "facebook/bart-large-mnli"
classifier = pipeline(
    "zero-shot-classification",
    model=MODEL_NAME
)

neutral_signals = [
    "urgency or time pressure",
    "request to perform an action",
    "request to click a link",
    "request to visit an external website",
    "request for personal information",
    "financial information or payment",
    "account or service issue",
]

high_risk_indicators = [
    "impersonation or false authority",
    "request for passwords, OTPs, or credentials",
    "threat of severe or catastrophic consequences",
    "coercive or threatening language",
    "unexpected financial opportunity",
    "unusual payment instructions",
    "attempt to obtain sensitive information",
]

contextual_markers = [
    "unexpected communication from the sender",
    "unusual communication channel",
    "unexpected request from an organization",
    "claim involving an existing account or service",
    "communication involving a trusted organization",
]


candidate_labels = (
    neutral_signals
    + high_risk_indicators
    + contextual_markers
)

# ============================================================
# Request DTO
# ============================================================

class AnalyzeRequest(BaseModel):
    content: str


# ============================================================
# Response DTO
# ============================================================

class AnalyzeResponse(BaseModel):
    neutral_score: float
    high_risk_score: float
    contextual_score: float

# ============================================================
# Helper function
# ============================================================

def calculate_category_average(
    result,
    category_labels
):
    """
    Calculate the average zero-shot score for one
    SignalBait semantic category.
    """

    scores = dict(
        zip(
            result["labels"],
            result["scores"]
        )
    )

    category_scores = [
        scores[label]
        for label in category_labels
        if label in scores
    ]

    if not category_scores:
        return 0.0

    return sum(category_scores) / len(category_scores)


# ============================================================
# NLP endpoint
# ============================================================

@app.post("/analyze", response_model=AnalyzeResponse)
def analyze_message(request: AnalyzeRequest):

    # Run the same zero-shot classification used during
    # the SignalBait experiments.
    result = classifier(
        request.content,
        candidate_labels,
        multi_label=True,
        hypothesis_template="This message contains {}."
    )

    # Convert the 19 individual semantic scores into
    # SignalBait's three categories.
    neutral_score = calculate_category_average(
        result,
        neutral_signals
    )

    high_risk_score = calculate_category_average(
        result,
        high_risk_indicators
    )

    contextual_score = calculate_category_average(
        result,
        contextual_markers
    )

    return AnalyzeResponse(
        neutral_score=neutral_score,
        high_risk_score=high_risk_score,
        contextual_score=contextual_score
    )


# ============================================================
# Health check
# ============================================================

@app.get("/health")
def health_check():

    return {
        "status": "ok",
        "service": "signalbait-nlp"
    }


# ============================================================
# OCR Service 
# ============================================================
# If the user selects image input in the application,
# this block of code will run to recognize text in the image,
# then the input is delivered to the main NLP pipeline.
ocr = PaddleOCR(
    use_doc_orientation_classify=False,
    use_doc_unwarping=False,
    use_textline_orientation=False,

    # Disable oneDNN/MKLDNN.
    # This avoids the PIR → oneDNN bug affecting
    # PaddlePaddle 3.3.x CPU inference.
    enable_mkldnn=False,
)

@app.post("/ocr")
async def ocr_image(file: UploadFile = File(...)):
    if not file.content_type or not file.content_type.startswith("image/"):
        raise HTTPException(
            status_code=400,
            detail="Only image files are supported."
        )

    image_bytes = await file.read()

    extension = Path(file.filename or "").suffix.lower()

    allowed_extensions = {
        ".bmp", ".dib", ".jpeg", ".jpg", ".png",
        ".webp", ".pbm", ".pgm", ".ppm", ".pnm",
        ".sr", ".ras", ".tiff", ".tif", ".pdf"
    }

    if extension not in allowed_extensions:
        raise HTTPException(
            status_code=400,
            detail="Unsupported image format."
        )

    temp_path = f"temp_ocr_image{extension}"

    try:
        # Temporarily save the image for PaddleOCR.
        with open(temp_path, "wb") as temp_file:
            temp_file.write(image_bytes)

        results = ocr.predict(temp_path)

        extracted_lines = []

        for result in results:
            # PaddleOCR 3.x exposes recognized text through rec_texts.
            result_data = result.json["res"]

            extracted_lines.extend(
                result_data["rec_texts"]
            )

        # Combine OCR lines into one message.
        extracted_text = "\n".join(extracted_lines)

        return {
            "text": extracted_text
        }

    finally:
        # Delete the uploaded image immediately.
        # SignalBait does not need to retain the screenshot.
        if os.path.exists(temp_path):
            os.remove(temp_path)

@app.post("/scan-image")
async def scan_image(file:UploadFile = File(...)):
    ocr_result = await ocr_image(file)
    nlp_result = analyze_message(
        AnalyzeRequest(content=ocr_result['text'])
    )

    return {
        "text": ocr_result["text"],
        "neutral_score": nlp_result.neutral_score,
        "high_risk_score": nlp_result.high_risk_score,
        "contextual_score": nlp_result.contextual_score
    }

