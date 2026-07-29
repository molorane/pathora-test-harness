package com.example.demo.dto;

public record LoanResponse(
        String applicationId,
        String applicantId,
        double approvedAmount,
        double interestRate,
        String decisionStatus,
        String evaluatedAt
) {
}
