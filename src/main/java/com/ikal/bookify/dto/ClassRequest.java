package com.ikal.bookify.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRequest {

    @NotBlank(message = "Class name is required")
    private String name;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "credits is required")
    private int creditsRequired;

    @NotBlank(message = "Start Time is required")
    private LocalDateTime scheduleTime;

    @NotBlank(message = "End Time is required")
    private LocalDateTime endTime;

    @NotBlank(message = "available slots is required")
    private int availableSlots;
}
