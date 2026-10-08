package com.example.demo.service;

import com.example.demo.dto.payment.PaymentRequest;
import com.example.demo.dto.payment.PaymentResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentGatewayService {

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);

        String status = paymentRequest.amount() > 0 ? "SUCCESS" : "FAILED";

        return new PaymentResponse(
                paymentId,
                paymentRequest.transactionId(),
                paymentRequest.amount(),
                status,
                Instant.now()
        );
    }
}


