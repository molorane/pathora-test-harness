package com.example.demo.dto;

public record InventoryRequest(
        String productId,
        int addQuantity,
        double unitPrice
) {
}
