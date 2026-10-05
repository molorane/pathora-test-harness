package com.example.demo.executor;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.time.Instant;
import java.util.UUID;

@Component
public class PaymentGatewayExecutor implements EntryPointExecutor<PaymentRequest, PaymentResponse> {

    @Override
    public String getEntryPointName() {
        return "payment-gateway-service";
    }

    @Override
    public Class<PaymentRequest> getRequestType() {
        return PaymentRequest.class;
    }

    @Override
    public PaymentResponse execute(PaymentRequest paymentRequest) {
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);

        String status = paymentRequest.amount() > 0 ? "SUCCESS" : "FAILED";

        return new PaymentResponse(
                paymentId,
                paymentRequest.transactionId(),
                paymentRequest.amount(),
                status,
                Instant.now().toString()
        );
    }
}
