package com.example.demo.dto.loan;

public record LoanRequest(
        String applicantId,
        double requestedAmount,
        int creditScore,
        int termMonths
) {
}

