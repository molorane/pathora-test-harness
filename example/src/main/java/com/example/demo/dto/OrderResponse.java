package com.example.demo.dto;

public record OrderResponse(
        String orderId,
        String customerId,
        int totalItems,
        double subtotal,
        double tax,
        double totalAmount,
        String status,
        String createdAt
) {
}
