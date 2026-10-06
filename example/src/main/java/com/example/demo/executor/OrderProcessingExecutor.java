package com.example.demo.executor;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.service.OrderProcessingService;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

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
