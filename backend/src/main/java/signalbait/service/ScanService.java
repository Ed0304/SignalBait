package signalbait.service;

import org.springframework.stereotype.Service;

import signalbait.dto.ScanResponse;
import signalbait.dto.nlp.NlpResult;

@Service
public class ScanService {

    private final MessageScanner messageScanner;

    public ScanService(MessageScanner messageScanner) {
        this.messageScanner = messageScanner;
    }

    public ScanResponse scan(String content) {

        // Send the message to the NLP layer.
        NlpResult nlpResult = messageScanner.analyze(content);

        // For now, we are only proving that the NLP result
        // successfully reaches the scanning service.
        System.out.println("Sentiment: " + nlpResult.getSentiment());
        System.out.println("Category scores: " + nlpResult.getCategoryScores());

        // Temporary response.
        return new ScanResponse(
            "HIGH",
            87,
            "Potential phishing attempt detected.",
            nlpResult
        );
    }
}