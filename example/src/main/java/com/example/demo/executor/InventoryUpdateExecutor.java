package com.example.demo.executor;

import com.example.demo.dto.InventoryRequest;
import com.example.demo.dto.InventoryResponse;
import com.example.demo.service.InventoryUpdateService;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

@Component
public class InventoryUpdateExecutor implements EntryPointExecutor<InventoryRequest, InventoryResponse> {

    private final InventoryUpdateService inventoryUpdateService;

    public InventoryUpdateExecutor(InventoryUpdateService inventoryUpdateService) {
        this.inventoryUpdateService = inventoryUpdateService;
    }

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
        return inventoryUpdateService.updateInventory(inventoryRequest);
    }
}
