package com.example.demo.executor;

import com.example.demo.dto.InventoryRequest;
import com.example.demo.dto.InventoryResponse;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.time.Instant;

@Component
public class InventoryUpdateExecutor implements EntryPointExecutor<InventoryRequest, InventoryResponse> {

    @Override
    public String getEntryPointName() {
        return "inventory-update-service";
    }

    @Override
    public Class<InventoryRequest> getRequestType() {
        return InventoryRequest.class;
    }

    @Override
    public InventoryResponse execute(InventoryRequest inventoryRequest) {
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
