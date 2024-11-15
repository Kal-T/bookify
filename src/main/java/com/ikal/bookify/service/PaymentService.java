package com.ikal.bookify.service;

import com.ikal.bookify.dto.CardInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public boolean addPaymentCard(CardInfo cardInfo) {
        return true; // Mock function
    }

    public boolean paymentCharge(String userId, double amount) {
        return true; // Mock function
    }
}
