package com.ikal.bookify.exception;

import com.ikal.bookify.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse response = new ApiResponse(
                "error",
                ex.getMessage(),
                "404",
                "User with the provided email not found"
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiResponse response = new ApiResponse(
                "error",
                "Token expired",
                "401",
                "The JWT token has expired"
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyUseException.class)
    public ResponseEntity<ApiResponse> handelEmailAlreadyUseException(EmailAlreadyUseException ex) {
        ApiResponse response = new ApiResponse(
                "error",
                "Registration failed.",
                "409",
                "Email is already Used."
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle BadCredentialsException (or any other exception)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse response = new ApiResponse(
                "error",
                "Invalid credentials",
                "401",
                "The email or password provided is incorrect."
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle General Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        ApiResponse response = new ApiResponse(
                "error",
                "Internal Server Error",
                "500",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
