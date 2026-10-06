package com.example.demo.dto;

public record LoanResponse(
    String applicationId,
    String applicantId,
    double approvedAmount,
    double interestRate,
    String decisionStatus,
    String evaluatedAt,
    boolean eligible,
    boolean requiresManualReview,
    Integer riskPenalty,
    int creditTierScore,
    String coSignerId
) {
}


