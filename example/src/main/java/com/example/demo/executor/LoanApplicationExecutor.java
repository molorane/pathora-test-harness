package com.example.demo.executor;

import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class LoanApplicationExecutor implements EntryPointExecutor {

    @Override
    public String getEntryPointName() {
        return "loan-application-service";
    }

    @Override
    public Class<?> getRequestType() {
        return LoanRequest.class;
    }

    @Override
    public Object execute(Object request) {
        LoanRequest loanRequest = (LoanRequest) request;
        String applicationId = "APP-" + UUID.randomUUID().toString().substring(0, 8);

        String decisionStatus;
        double interestRate;
        double approvedAmount;

        if (loanRequest.creditScore() >= 700) {
            decisionStatus = "APPROVED";
            interestRate = 6.5;
            approvedAmount = loanRequest.requestedAmount();
        } else if (loanRequest.creditScore() >= 600) {
            decisionStatus = "APPROVED_CONDITIONAL";
            interestRate = 9.5;
            approvedAmount = loanRequest.requestedAmount() * 0.8;
        } else {
            decisionStatus = "REJECTED";
            interestRate = 0.0;
            approvedAmount = 0.0;
        }

        return new LoanResponse(
                applicationId,
                loanRequest.applicantId(),
                approvedAmount,
                interestRate,
                decisionStatus,
                Instant.now().toString()
        );
    }
}
