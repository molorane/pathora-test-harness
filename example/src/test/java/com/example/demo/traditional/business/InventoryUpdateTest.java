package com.example.demo.traditional.business;

import com.example.demo.dto.inventory.InventoryRequest;
import com.example.demo.dto.inventory.InventoryResponse;
import com.example.demo.traditional.TraditionalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryUpdateTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Inventory Stock Restock Test")
    void inventoryStockRestockTest() throws Exception {
        InventoryResponse response = updateInventory(request -> new InventoryRequest("SKU-90882", 250, 18.75));

        assertAll(
                () -> assertEquals("SKU-90882", response.productId(), "Product ID must match updated value"),
                () -> assertTrue(response.stockLevel() >= 200, "Stock level must be at least 200 units"),
                () -> assertEquals("IN_STOCK", response.stockStatus(), "Stock status must be IN_STOCK"),
                () -> assertEquals(18.75, response.unitPrice(), 0.0001, "Unit price must match input unit price")
        );
    }
}
