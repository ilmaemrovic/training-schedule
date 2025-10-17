package raf.microservice.components.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDto {

    @NotBlank(message = "Old password cannot be empty")
    @Size(min = 8, max = 16, message = "Old password must be between 8 and 16 characters")
    private String oldPassword;

    @NotBlank(message = "New password cannot be empty")
    @Size(min = 8, max = 16, message = "New password must be between 8 and 16 characters")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
