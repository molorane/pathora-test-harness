package com.example.demo.dto;

public record LoanRequest(
        String applicantId,
        double requestedAmount,
        int creditScore,
        int termMonths
) {
}
