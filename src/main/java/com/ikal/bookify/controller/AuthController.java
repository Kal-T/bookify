package com.ikal.bookify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ikal.bookify.dto.*;
import com.ikal.bookify.service.AuthService;
import com.ikal.bookify.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        return ResponseEntity.ok("Ok desu.");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new ApiResponse("Success", "Authentication successful",jwtResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok(new ApiResponse("Success", "User registration is successful. Please verify your email."));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiResponse> requestPasswordReset(@RequestBody InitialPasswordResetRequest passwordResetRequest) {
        String token = passwordResetService.createPasswordResetToken(passwordResetRequest.getEmail());
        // should return like that
        //  return ResponseEntity.ok("Password reset link sent to your email");

        return ResponseEntity.ok(new ApiResponse("Success", "Authentication successful",new InitialPasswordResetResponse(token)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest resetPasswordRequest) {
        boolean isValid = passwordResetService.validateResetToken(resetPasswordRequest.getToken());
        if (isValid) {
            passwordResetService.updatePassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
            return ResponseEntity.ok(new ApiResponse("Success", "Password updated successfully"));
        }
        return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Invalid token or token expired"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Token not found."));
        }

        Optional<JwtResponse> jwtResponse = authService.refreshToken(authHeader);
        if (jwtResponse.isPresent()) {
            // Token is valid, you can use the JwtResponse here
            return ResponseEntity.ok(new ApiResponse("Success", "Authentication successful",jwtResponse));
        } else {
            return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Token not found."));

        }
    }
}