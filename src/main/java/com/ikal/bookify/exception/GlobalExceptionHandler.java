package com.ikal.bookify.exception;

import com.ikal.bookify.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    // Handle General Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(Exception ex) {
        ApiResponse response = new ApiResponse(
                "error",
                "Resource Not Found",
                "404",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle General Exception
    @ExceptionHandler(PurchasePackageFailException.class)
    public ResponseEntity<ApiResponse> handlePurchasePackageFailException(Exception ex) {
        ApiResponse response = new ApiResponse(
                "error",
                "Purchase Package Fail",
                "409",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Validation failed");
        response.put("errorCode", "400");

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        response.put("details", validationErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
