package signalbait.dto;

import jakarta.validation.constraints.NotBlank;
//This class describes how a valid request look like.
public class ScanRequest {

    @NotBlank //Constraint telling content string must not be blank.
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}