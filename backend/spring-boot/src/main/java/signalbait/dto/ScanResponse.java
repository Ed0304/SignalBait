package signalbait.dto;

import signalbait.dto.nlp.NlpResult;

public class ScanResponse {
    
    private String riskLevel;
    private int score;
    private String message;
    private NlpResult nlpResult;

    public ScanResponse(String riskLevel,int score, String message, NlpResult nlpResult){
        this.riskLevel = riskLevel;
        this.score = score;
        this.message = message;
        this.nlpResult = nlpResult;
    }
    //Getters
    public String getRiskLevel(){
        return riskLevel;
    }
    public int getScore(){
        return score;
    }
    public String getMessage(){
        return message;
    }
    public NlpResult getNlpResult(){
        return nlpResult;
    }

    //Setters
    public void setRiskLevel(String riskLevel){
        this.riskLevel = riskLevel;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setNlpResult(NlpResult nlpResult){
        this.nlpResult = nlpResult;
    }
}
