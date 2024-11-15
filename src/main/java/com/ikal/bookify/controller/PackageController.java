package com.ikal.bookify.controller;

import com.ikal.bookify.dto.ApiResponse;
import com.ikal.bookify.dto.CardInfo;
import com.ikal.bookify.dto.PurchaseRequest;
import com.ikal.bookify.model.Package;
import com.ikal.bookify.model.UserPackage;
import com.ikal.bookify.service.PackageService;
import com.ikal.bookify.service.PaymentService;
import com.ikal.bookify.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailablePackages(@RequestParam String country) {
        List<Package> packages = packageService.getAvailablePackages(country);
        return ResponseEntity.ok(new ApiResponse("Success", "Get Aavailable Packages",packages));
    }

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

    @GetMapping("/my-packages")
    public ResponseEntity<ApiResponse> getUserPackages(
            HttpServletRequest request,
            HttpServletResponse response) {
        ResponseEntity<?> extractToken = Util.extractUserIdAndValidateToken(request);
        if (extractToken.getBody() instanceof ApiResponse) {
            return (ResponseEntity<ApiResponse>) response;
        }
        Long userId = (Long) extractToken.getBody();
        List<UserPackage> userPackages = packageService.getUserPackages(userId);
        return ResponseEntity.ok(new ApiResponse("Success", "Get user avaialbe packages successfully",userPackages));
    }
}

