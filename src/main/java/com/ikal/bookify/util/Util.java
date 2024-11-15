package com.ikal.bookify.util;

import com.ikal.bookify.dto.ApiResponse;
import com.ikal.bookify.service.auth.JwtService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Util {
    @Autowired
    private JwtService jwtServiceInstance;

    private static JwtService jwtService;

    @PostConstruct
    public void init() {
        jwtService = jwtServiceInstance;
    }

    public static ResponseEntity<?> extractAndValidateToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Token not found."));
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username == null) {
            return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Invalid Token."));
        }

        return ResponseEntity.ok(username);
    }

    public static ResponseEntity<?> extractUserIdAndValidateToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Token not found."));
        }

        String token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        if (userId == null) {
            return ResponseEntity.ok(new ApiResponse("Error", "Invalid Credentials", "401", "Invalid Token."));
        }

        return ResponseEntity.ok(userId);
    }

}
