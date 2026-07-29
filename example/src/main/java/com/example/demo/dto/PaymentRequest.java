package com.example.demo.dto;

public record PaymentRequest(
        String transactionId,
        double amount,
        String currency,
        String paymentMethod
) {
}
