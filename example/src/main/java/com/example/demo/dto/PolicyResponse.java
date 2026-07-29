package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public record PolicyResponse(
        String policyNumber,
        String status,
        String evaluationTimestamp,
        String expirationTimestamp,
        String policyHeaderEffectiveDate,
        double basePremium,
        double totalPremium,
        double discountAmount,
        double finalPrice,
        int appliedClausesCount,
        int riskScore,
        int insuredRiskScore,
        String insuredPartyLegalName,
        String creditRating,
        String underwriterRegion,
        String primaryContactEmail,
        List<ApprovedClause> approvedClauses,
        List<String> requiredDocs,
        List<String> singlePrimaryAuditor,
        List<String> approvedTags,
        List<String> emptyExclusions,
        List<String> uniqueReferenceCodes,
        List<String> riskFlags,
        Map<String, Object> underwritingSummary,
        Map<String, Object> underwriterSummary
) {
    public record ApprovedClause(
            String clauseId,
            String status,
            double limit
    ) {
    }
}
