package com.example.demo.executor;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.time.Instant;
import java.util.UUID;

@Component
public class OrderProcessingExecutor implements EntryPointExecutor<OrderRequest, OrderResponse> {

    @Override
    public String getEntryPointName() {
        return "order-processing-service";
    }

    @Override
    public Class<OrderRequest> getRequestType() {
        return OrderRequest.class;
    }

    @Override
    public OrderResponse execute(OrderRequest orderRequest) {
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
                Instant.now().toString()
        );
    }
}
