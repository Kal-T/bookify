package com.ikal.bookify.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public boolean sendVerificationEmail(String email) {
        return true; // Mock function
    }

    public boolean sendRefundEmail(String email) {
        return true; // Mock function
    }

    public boolean sendCheckInConfirmation(String email) {
        return true; // Mock function
    }

    public boolean sendWaitlistCancellationEmail(String email) {
        return true;
    }
}
