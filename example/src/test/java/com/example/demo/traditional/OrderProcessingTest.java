package com.example.demo.traditional;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderProcessingTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Order Checkout Calculation Test")
    void orderCheckoutCalculationTest() throws Exception {
        OrderResponse response = processOrder(request -> new OrderRequest("CUST-99001", request.items(), "USD"));

        assertAll(
                () -> assertTrue(response.orderId().startsWith("ORD-"), "Order ID must start with ORD-"),
                () -> assertEquals("CUST-99001", response.customerId(), "Customer ID must match request parameter"),
                () -> assertEquals(3, response.totalItems(), "Total items count must be 3"),
                () -> assertTrue(response.subtotal() > 100.0, "Subtotal must be greater than 100.0"),
                () -> assertEquals("CREATED", response.status(), "Order status must be CREATED")
        );
    }
}

