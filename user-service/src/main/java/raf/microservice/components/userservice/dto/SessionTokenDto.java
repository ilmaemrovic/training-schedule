package raf.microservice.components.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionTokenDto {
    @JsonProperty("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
