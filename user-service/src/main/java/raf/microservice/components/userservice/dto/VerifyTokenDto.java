package raf.microservice.components.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyTokenDto {
    @JsonProperty("verify_token")
    private String verifyToken;

    public VerifyTokenDto(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public VerifyTokenDto() {
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }
}
