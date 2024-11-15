package com.ikal.bookify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ikal.bookify.dto.*;
import com.ikal.bookify.service.AuthService;
import com.ikal.bookify.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Auth", description = "Auth Endpoint")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Operation(
            description = "This is a Bookify API Testing Endpoint",
            summary = "Welcome Test API",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        return ResponseEntity.ok(new ApiResponse("Success", "Welcome to Bookify API v1.0"));
    }

    @Operation(
            description = "This is a Bookify API Login Endpoint",
            summary = "Login",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "The email or password provided is incorrect.",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )

    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new ApiResponse("Success", "Authentication successful",jwtResponse));
    }

    @Operation(
            description = "This is a Bookify API Register Endpoint",
            summary = "Register",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Email is already Used.",
                            responseCode = "409",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok(new ApiResponse("Success", "User registration is successful. Please verify your email."));
    }

    @Operation(
            description = "This is a Bookify API Password Reset Request Endpoint",
            summary = "Password Reset Request",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "User with the provided email not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = InitialPasswordResetRequest.class))
            )

    )
    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiResponse> requestPasswordReset(@RequestBody InitialPasswordResetRequest passwordResetRequest) {
        String token = passwordResetService.createPasswordResetToken(passwordResetRequest.getEmail());
        // should return like that
        //  return ResponseEntity.ok("Password reset link sent to your email");
        return ResponseEntity.ok(new ApiResponse("Success", "Request successful",new InitialPasswordResetResponse(token)));
    }

    @Operation(
            description = "This is a Bookify API Register Endpoint",
            summary = "Register",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Invalid token or token expired",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = PasswordResetRequest.class))
            )

    )
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest resetPasswordRequest) {
        boolean isValid = passwordResetService.validateResetToken(resetPasswordRequest.getToken());
        if (isValid) {
            passwordResetService.updatePassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
            return ResponseEntity.ok(new ApiResponse("Success", "Password updated successfully"));
        }
        return ResponseEntity.ok(new ApiResponse("Error", "Token expired", "401", "Invalid token or token expired"));
    }

    @Operation(
            description = "This is a Bookify API Refresh Token Endpoint",
            summary = "Refresh Token",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Token not found.",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
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

    @Operation(
            description = "This is a Bookify API Logout Endpoint",
            summary = "Logout",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Token not found.",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Token not found."));
        }

        authService.logout(authHeader);
        return ResponseEntity.ok(new ApiResponse("Success", "Logout successful"));
    }
}