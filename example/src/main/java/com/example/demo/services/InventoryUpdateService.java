package com.example.demo.services;

import com.example.demo.dto.InventoryRequest;
import com.example.demo.dto.InventoryResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class InventoryUpdateService {

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
