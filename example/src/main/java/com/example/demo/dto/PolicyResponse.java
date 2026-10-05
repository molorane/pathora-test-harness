package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public record PolicyResponse(
        String policyNumber,
        String status,
        String evaluationTimestamp,
        String expirationTimestamp,
        String policyHeaderEffectiveDate,
        String currentDate,
        String currentDateTime,
        String futureDateTime,
        double basePremium,
        double totalPremium,
        double discountAmount,
        double finalPrice,
        int appliedClausesCount,
        int riskScore,
        int penaltyPoints,
        int insuredRiskScore,
        boolean active,
        boolean suspended,
        String optionalNullField,
        String auditUuid,
        String portalUrl,
        String serverIp,
        String companyName,
        String alphaCategory,
        String mixedCaseNotes,
        String blankNotes,
        String emptyNotes,
        String numericPostalCode,
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
        List<String> descendingCodes,
        List<String> allBlankList,
        List<String> mixedBlankList,
        List<String> noneBlankList,
        List<String> riskFlags,
        Map<String, Object> underwritingSummary,
        Map<String, Object> underwriterSummary,
        Map<String, Object> emptyMetadata
) {
    public record ApprovedClause(
            String clauseId,
            String status,
            double limit
    ) {
    }
}


