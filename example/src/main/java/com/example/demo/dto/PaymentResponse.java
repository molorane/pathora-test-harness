package com.example.demo.dto;

public record PaymentResponse(
        String paymentId,
        String transactionId,
        double amount,
        String status,
        String timestamp
) {
}
