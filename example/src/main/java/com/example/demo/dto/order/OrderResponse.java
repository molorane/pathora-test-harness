package com.example.demo.dto.order;

import java.time.Instant;

public record OrderResponse(
    String orderId,
    String customerId,
    int totalItems,
    double subtotal,
    double tax,
    double totalAmount,
    String status,
    Instant createdAt
) {
}

