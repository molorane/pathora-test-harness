package com.example.demo.executor;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.service.PaymentGatewayService;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayExecutor implements EntryPointExecutor<PaymentRequest, PaymentResponse> {

    private final PaymentGatewayService paymentGatewayService;

    public PaymentGatewayExecutor(PaymentGatewayService paymentGatewayService) {
        this.paymentGatewayService = paymentGatewayService;
    }

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
        return paymentGatewayService.processPayment(paymentRequest);
    }
}
