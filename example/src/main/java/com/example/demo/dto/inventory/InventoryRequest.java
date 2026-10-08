package com.example.demo.dto.inventory;

public record InventoryRequest(
        String productId,
        int addQuantity,
        double unitPrice
) {
}

