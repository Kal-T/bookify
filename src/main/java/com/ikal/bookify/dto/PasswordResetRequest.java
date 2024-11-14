package com.ikal.bookify.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotNull(message = "Token cannot be null")
    private String token;

    @NotNull(message = "New password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}
