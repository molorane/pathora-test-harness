package com.example.demo.executor;

import com.example.demo.dto.InventoryRequest;
import com.example.demo.dto.InventoryResponse;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InventoryUpdateExecutor implements EntryPointExecutor {

    @Override
    public String getEntryPointName() {
        return "inventory-update-service";
    }

    @Override
    public Class<?> getRequestType() {
        return InventoryRequest.class;
    }

    @Override
    public Object execute(Object request) {
        InventoryRequest inventoryRequest = (InventoryRequest) request;
        int newStockLevel = Math.max(0, inventoryRequest.addQuantity());
        String stockStatus = newStockLevel > 0 ? "IN_STOCK" : "OUT_OF_STOCK";

        return new InventoryResponse(
                inventoryRequest.productId(),
                newStockLevel,
                stockStatus,
                inventoryRequest.unitPrice(),
                Instant.now().toString()
        );
    }
}
