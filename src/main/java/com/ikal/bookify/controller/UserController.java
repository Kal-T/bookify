package com.ikal.bookify.controller;

import com.ikal.bookify.dto.*;
import com.ikal.bookify.model.User;
import com.ikal.bookify.service.UserService;
import com.ikal.bookify.service.auth.JwtService;
import com.ikal.bookify.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "User", description = "User Endpoint")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(
            description = "This is a Bookify API Get Profile Endpoint",
            summary = "User Profile",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "User not found.",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile(HttpServletRequest request,
                                               HttpServletResponse response) {

        ResponseEntity<?> extractToken = Util.extractAndValidateToken(request);
        if (extractToken.getBody() instanceof ApiResponse) {
            return (ResponseEntity<ApiResponse>) response;
        }
        String username = (String) extractToken.getBody();
        Optional<UserInfo> userInfo = userService.getUserProfile(username);
        if (userInfo.isPresent()) {
            // Token is valid, you can use the JwtResponse here
            return ResponseEntity.ok(new ApiResponse("Success", "Get User Profile successfully",userInfo));
        } else {
            return ResponseEntity.ok(new ApiResponse("Error", "Unexpected error", "404", "Users not found."));

        }
    }

    @Operation(
            description = "This is a Bookify API Change Password Endpoint",
            summary = "Change Password",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Incorrect password.",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class))
            )

    )
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {

        ResponseEntity<?> extractToken = Util.extractAndValidateToken(request);
        if (extractToken.getBody() instanceof ApiResponse) {
            return (ResponseEntity<ApiResponse>) response;
        }
        String username = (String) extractToken.getBody();
        boolean isPasswordChanged = userService.changePassword(
                username,
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword());

        if (isPasswordChanged) {
            return ResponseEntity.ok(new ApiResponse("Success", "Password changed successfully."));
        } else {
            return ResponseEntity.ok(new ApiResponse("Error", "Unexpected error", "401", "Incorrect password."));
        }
    }
}
