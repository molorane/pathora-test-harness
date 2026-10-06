package com.example.demo.dto;

import java.time.Instant;

public record PaymentResponse(
        String paymentId,
        String transactionId,
        double amount,
        String status,
        Instant timestamp
) {
}
