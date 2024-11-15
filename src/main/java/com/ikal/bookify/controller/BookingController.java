package com.ikal.bookify.controller;

import com.ikal.bookify.dto.ApiResponse;
import com.ikal.bookify.exception.BookingException;
import com.ikal.bookify.service.BookingService;
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

@Tag(name = "Booking", description = "Booking Endpoint")
@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(
            description = "This is a Bookify API Booking Class Endpoint",
            summary = "Booking Class",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @PostMapping("/book")
    public ResponseEntity<ApiResponse> bookClass(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Long classId) {
        ResponseEntity<?> extractToken = Util.extractUserIdAndValidateToken(request);
        if (extractToken.getBody() instanceof ApiResponse) {
            return (ResponseEntity<ApiResponse>) response;
        }
        Long userId = (Long) extractToken.getBody();
        String result = bookingService.bookClass(userId, classId);
        return ResponseEntity.ok(new ApiResponse("Success", "Class was booked successfully"));
    }

    @Operation(
            description = "This is a Bookify API Check-in Booking Endpoint",
            summary = "Check-in Booking",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse> checkIn(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Long bookingId) {
            ResponseEntity<?> extractToken = Util.extractUserIdAndValidateToken(request);
            if (extractToken.getBody() instanceof ApiResponse) {
                return (ResponseEntity<ApiResponse>) response;
            }
            Long userId = (Long) extractToken.getBody();
            bookingService.checkIn(userId, bookingId);
            return ResponseEntity.ok(new ApiResponse("Success", "Booking was checked-in successfully"));
    }

    @Operation(
            description = "This is a Bookify API Cancel Booking Endpoint",
            summary = "Cancel Booking",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @PostMapping("/booking-cancel")
    public ResponseEntity<ApiResponse> cancelBooking(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Long bookingId) {

            ResponseEntity<?> extractToken = Util.extractUserIdAndValidateToken(request);
            if (extractToken.getBody() instanceof ApiResponse) {
                return (ResponseEntity<ApiResponse>) response;
            }
            Long userId = (Long) extractToken.getBody();

            bookingService.cancelBooking(userId, bookingId);
            return ResponseEntity.ok(new ApiResponse("Success", "Booking was canceled successfully"));
    }

    @Operation(
            description = "This is a Bookify API Cancel Waitlist Endpoint",
            summary = "Cancel Waitlist",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @PostMapping("/waitlist-cancel")
    public ResponseEntity<ApiResponse> cancelWaitlist(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Long classId) {

        ResponseEntity<?> extractToken = Util.extractUserIdAndValidateToken(request);
        if (extractToken.getBody() instanceof ApiResponse) {
            return (ResponseEntity<ApiResponse>) response;
        }
        Long userId = (Long) extractToken.getBody();

        bookingService.cancelWaitlist(userId, classId);
        return ResponseEntity.ok(new ApiResponse("Success", "Waitlist was canceled successfully"));
    }
}
