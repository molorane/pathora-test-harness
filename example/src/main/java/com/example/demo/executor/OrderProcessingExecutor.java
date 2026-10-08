package com.example.demo.executor;

import com.example.demo.dto.order.OrderRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.service.OrderProcessingService;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

@Component
public class OrderProcessingExecutor implements EntryPointExecutor<OrderRequest, OrderResponse> {

    private final OrderProcessingService orderProcessingService;

    public OrderProcessingExecutor(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

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
        return orderProcessingService.processOrder(orderRequest);
    }
}

