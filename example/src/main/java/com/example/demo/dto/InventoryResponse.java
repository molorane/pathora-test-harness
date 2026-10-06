package com.example.demo.dto;

import java.time.Instant;

public record InventoryResponse(
    String productId,
    int stockLevel,
    String stockStatus,
    double unitPrice,
    Instant lastUpdated
) {
}
