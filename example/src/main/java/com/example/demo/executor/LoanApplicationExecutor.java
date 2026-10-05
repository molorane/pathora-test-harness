package com.example.demo.executor;

import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.time.Instant;
import java.util.UUID;

@Component
public class LoanApplicationExecutor implements EntryPointExecutor<LoanRequest, LoanResponse> {

    @Override
    public String getEntryPointName() {
        return "loan-application-service";
    }

    @Override
    public Class<LoanRequest> getRequestType() {
        return LoanRequest.class;
    }

    @Override
    public LoanResponse execute(LoanRequest loanRequest) {
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

        boolean eligible = "APPROVED".equals(decisionStatus) || "APPROVED_CONDITIONAL".equals(decisionStatus);
        boolean requiresManualReview = "APPROVED_CONDITIONAL".equals(decisionStatus);

        return new LoanResponse(
                applicationId,
                loanRequest.applicantId(),
                approvedAmount,
                interestRate,
                decisionStatus,
                Instant.now().toString(),
                eligible,
                requiresManualReview,
                -10,
                loanRequest.creditScore(),
                null
        );
    }
}


