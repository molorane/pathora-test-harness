package com.example.demo.service;

import com.example.demo.dto.loan.LoanRequest;
import com.example.demo.dto.loan.LoanResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class LoanApplicationService {

    public LoanResponse processApplication(LoanRequest loanRequest) {
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
                Instant.now(),
                eligible,
                requiresManualReview,
                -10,
                loanRequest.creditScore(),
                null
        );
    }
}


