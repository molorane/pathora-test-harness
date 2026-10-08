package com.example.demo.dto.payment;

public record PaymentRequest(
        String transactionId,
        double amount,
        String currency,
        String paymentMethod
) {
}

