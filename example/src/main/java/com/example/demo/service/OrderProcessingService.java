package com.example.demo.service;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderProcessingService {

    public OrderResponse processOrder(OrderRequest orderRequest) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

        double subtotal = 0.0;
        int totalItems = 0;
        if (orderRequest.items() != null) {
            for (OrderRequest.OrderItem item : orderRequest.items()) {
                subtotal += item.price() * item.quantity();
                totalItems += item.quantity();
            }
        }

        double tax = Math.round(subtotal * 0.15 * 100.0) / 100.0;
        double totalAmount = Math.round((subtotal + tax) * 100.0) / 100.0;

        return new OrderResponse(
                orderId,
                orderRequest.customerId(),
                totalItems,
                subtotal,
                tax,
                totalAmount,
                "CREATED",
                Instant.now()
        );
    }
}

