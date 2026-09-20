package signalbait.dto;

public class ScanResponse {
    
    private String riskLevel;
    private int score;
    private String message;

    public ScanResponse(String riskLevel,int score, String message){
        this.riskLevel = riskLevel;
        this.score = score;
        this.message = message;
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
}
