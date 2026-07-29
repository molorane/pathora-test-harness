package com.example.demo.dto;

public record InventoryResponse(
        String productId,
        int stockLevel,
        String stockStatus,
        double unitPrice,
        String lastUpdated
) {
}
