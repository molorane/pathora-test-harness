package com.example.demo.executor;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class PaymentGatewayExecutor implements EntryPointExecutor {

    @Override
    public String getEntryPointName() {
        return "payment-gateway-service";
    }

    @Override
    public Class<?> getRequestType() {
        return PaymentRequest.class;
    }

    @Override
    public Object execute(Object request) {
        PaymentRequest paymentRequest = (PaymentRequest) request;
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
