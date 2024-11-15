package com.ikal.bookify.controller;

import com.ikal.bookify.dto.ApiResponse;
import com.ikal.bookify.dto.CardInfo;
import com.ikal.bookify.dto.ChangePasswordRequest;
import com.ikal.bookify.dto.PurchaseRequest;
import com.ikal.bookify.model.Package;
import com.ikal.bookify.model.UserPackage;
import com.ikal.bookify.service.PackageService;
import com.ikal.bookify.service.PaymentService;
import com.ikal.bookify.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Package", description = "Package Endpoint")
@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Operation(
            description = "This is a Bookify API Available Packages Endpoint",
            summary = "Available Packages",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailablePackages(@RequestParam String country) {
        List<Package> packages = packageService.getAvailablePackages(country);
        return ResponseEntity.ok(new ApiResponse("Success", "Get Aavailable Packages",packages));
    }

    @Operation(
            description = "This is a Bookify API Purchase Package Endpoint",
            summary = "Purchase Package",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Resource Not Found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Purchase Package Fail",
                            responseCode = "409",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = PurchaseRequest.class))
            )

    )
    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse> purchasePackage(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody PurchaseRequest purchaseRequest) {
        ResponseEntity<?> extractToken = Util.extractUserIdAndValidateToken(request);
        if (extractToken.getBody() instanceof ApiResponse) {
            return (ResponseEntity<ApiResponse>) response;
        }
        Long userId = (Long) extractToken.getBody();
        UserPackage userPackage = packageService.purchasePackage(userId, purchaseRequest);
        return ResponseEntity.ok(new ApiResponse("Success", "Package purchase is successfully completed."));
    }

    @Operation(
            description = "This is a Bookify API User Purchased Package Endpoint",
            summary = "User Package",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @GetMapping("/my-packages")
    public ResponseEntity<ApiResponse> getUserPackages(
            HttpServletRequest request,
            HttpServletResponse response) {
        ResponseEntity<?> extractToken = Util.extractUserIdAndValidateToken(request);
        if (extractToken.getBody() instanceof ApiResponse) {
            return (ResponseEntity<ApiResponse>) response;
        }
        Long userId = (Long) extractToken.getBody();
        List<Package> packages = packageService.getUserPackages(userId);
        return ResponseEntity.ok(new ApiResponse("Success", "Get user avaialbe packages successfully",packages));
    }
}

