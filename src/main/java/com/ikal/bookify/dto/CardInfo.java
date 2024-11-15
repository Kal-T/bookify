package com.ikal.bookify.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardInfo {

    @NotBlank(message = "Card Number is required")
    private String cardNumber;

    @NotBlank(message = "Card Holder Name is required")
    private String cardHolderName;

    @NotBlank(message = "Expiry Date is required")
    private String expiryDate;

    @NotBlank(message = "CVV is required")
    private String cvv;

    public CardInfo(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

}

