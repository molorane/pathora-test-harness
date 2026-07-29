package com.example.demo.dto;

import java.util.List;

public record PolicyRequest(
        PolicyHeader policyHeader,
        InsuredParty insuredParty,
        CoverageDetails coverageDetails
) {
    public record PolicyHeader(
            String policyNumber,
            String effectiveDate,
            String expiryDate,
            Underwriter underwriter
    ) {
    }

    public record Underwriter(
            String code,
            String region,
            Contact contact
    ) {
    }

    public record Contact(
            String email,
            String phone
    ) {
    }

    public record InsuredParty(
            String entityId,
            String legalName,
            String registrationNumber,
            RiskMetrics riskMetrics
    ) {
    }

    public record RiskMetrics(
            String creditRating,
            int score,
            int priorClaimsCount,
            List<String> flaggedRisks
    ) {
    }

    public record CoverageDetails(
            double basePremium,
            double deductible,
            List<Clause> clauses,
            List<Endorsement> endorsements
    ) {
    }

    public record Clause(
            String clauseId,
            String title,
            double limit,
            boolean mandatory,
            List<String> tags
    ) {
    }

    public record Endorsement(
            String code,
            double fee
    ) {
    }
}
