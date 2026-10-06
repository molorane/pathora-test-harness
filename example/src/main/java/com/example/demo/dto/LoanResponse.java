package com.example.demo.dto;

import java.time.Instant;

public record LoanResponse(
        String applicationId,
        String applicantId,
        double approvedAmount,
        double interestRate,
        String decisionStatus,
        Instant evaluatedAt,
        boolean eligible,
        boolean requiresManualReview,
        Integer riskPenalty,
        int creditTierScore,
        String coSignerId
) {
}


