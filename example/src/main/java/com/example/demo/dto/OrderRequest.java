package com.example.demo.dto;

import java.util.List;

public record OrderRequest(
        String customerId,
        List<OrderItem> items,
        String currency
) {
    public record OrderItem(
            String productId,
            int quantity,
            double price
    ) {
    }
}
