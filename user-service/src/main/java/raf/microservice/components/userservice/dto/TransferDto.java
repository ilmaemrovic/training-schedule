package raf.microservice.components.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashMap;

public class TransferDto {

    @NotBlank(message = "Email cannot be empty")
    @Email
    private String emailReceiver;

    @NotBlank(message = "Type name can not be empty")
    private String typeName;

    private HashMap<String, String> params;

    @NotBlank
    private String username;

    public TransferDto(String emailReceiver, String typeName, HashMap<String, String> params, String username) {
        this.emailReceiver = emailReceiver;
        this.typeName = typeName;
        this.params = params;
        this.username = username;
    }

    @Override
    public String toString() {
        return "TransferDto{" +
                "emailReceiver='" + emailReceiver + '\'' +
                ", typeName='" + typeName + '\'' +
                ", params=" + params +
                ", username='" + username + '\'' +
                '}';
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
