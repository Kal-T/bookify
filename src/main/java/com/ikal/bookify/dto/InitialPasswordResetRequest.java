package com.ikal.bookify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InitialPasswordResetRequest {

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
}
