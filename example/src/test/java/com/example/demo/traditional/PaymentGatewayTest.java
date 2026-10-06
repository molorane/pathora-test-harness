package com.example.demo.traditional;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PaymentGatewayTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Successful Payment Processing Test")
    void successfulPaymentProcessingTest() throws Exception {
        PaymentResponse response = processPayment(request -> new PaymentRequest(request.transactionId(), 499.99, request.currency(), "DEBIT_CARD"));

        assertAll(
                () -> assertTrue(response.paymentId().startsWith("PAY-"), "Payment ID must be generated with PAY- prefix"),
                () -> assertEquals("TXN-998811", response.transactionId(), "Transaction ID must match input request"),
                () -> assertTrue(response.amount() > 0.0, "Payment amount must be greater than zero"),
                () -> assertEquals("SUCCESS", response.status(), "Payment status must be SUCCESS")
        );
    }
}

