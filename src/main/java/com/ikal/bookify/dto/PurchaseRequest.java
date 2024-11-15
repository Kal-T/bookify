package com.ikal.bookify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {

    @NotBlank(message = "Package ID is required")
    private Long packageId;

    @NotBlank(message = "CardInfo is required")
    private CardInfo cardInfo;
}
