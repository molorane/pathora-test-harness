package com.example.demo.service;

import com.example.demo.dto.policy.PolicyRequest;
import com.example.demo.dto.policy.PolicyResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ComplexPolicyService {

    private static final LocalDateTime EVALUATION_TIMESTAMP = LocalDateTime.of(2026, 9, 15, 12, 30, 45);
    private static final LocalDateTime EXPIRATION_TIMESTAMP = LocalDateTime.of(2027, 9, 15, 12, 30, 45);
    private static final LocalDate DEFAULT_POLICY_EFFECTIVE_DATE = LocalDate.of(2026, 1, 1);

    public PolicyResponse evaluatePolicy(PolicyRequest req) {
        String policyNumber = req.policyHeader() != null && req.policyHeader().policyNumber() != null
            ? req.policyHeader().policyNumber()
            : "POL-2026-DEFAULT";

        int riskScore = 820;
        List<String> riskFlags = Collections.emptyList();
        String legalName = "Pathora Enterprise Solutions";
        String creditRating = "AA+";

        if (req.insuredParty() != null) {
            if (req.insuredParty().legalName() != null) {
                legalName = req.insuredParty().legalName();
            }
            if (req.insuredParty().riskMetrics() != null) {
                riskScore = req.insuredParty().riskMetrics().score();
                if (req.insuredParty().riskMetrics().flaggedRisks() != null) {
                    riskFlags = req.insuredParty().riskMetrics().flaggedRisks();
                }
                if (req.insuredParty().riskMetrics().creditRating() != null) {
                    creditRating = req.insuredParty().riskMetrics().creditRating();
                }
            }
        }

        double basePremium = 15000.00;
        double endorsementFees = 0.0;
        List<PolicyResponse.ApprovedClause> approvedClauses = new ArrayList<>();
        List<String> tags = new ArrayList<>();

        if (req.coverageDetails() != null) {
            if (req.coverageDetails().basePremium() > 0) {
                basePremium = req.coverageDetails().basePremium();
            }
            if (req.coverageDetails().endorsements() != null) {
                for (PolicyRequest.Endorsement end : req.coverageDetails().endorsements()) {
                    endorsementFees += end.fee();
                }
            }
            if (req.coverageDetails().clauses() != null) {
                for (PolicyRequest.Clause clause : req.coverageDetails().clauses()) {
                    approvedClauses.add(new PolicyResponse.ApprovedClause(
                        clause.clauseId(), "APPROVED", clause.limit()
                    ));
                    if (clause.tags() != null) {
                        tags.addAll(clause.tags());
                    }
                }
            }
        }

        double totalPremium = basePremium + endorsementFees;
        double discountAmount = 1000.00;
        double finalPrice = totalPremium - discountAmount;

        String underwriterRegion = "AFRICA_SOUTH";
        String contactEmail = "underwriting@pathora.co.za";
        String underwriterCode = "UW-NORTH-99";

        if (req.policyHeader() != null && req.policyHeader().underwriter() != null) {
            if (req.policyHeader().underwriter().region() != null) {
                underwriterRegion = req.policyHeader().underwriter().region();
            }
            if (req.policyHeader().underwriter().code() != null) {
                underwriterCode = req.policyHeader().underwriter().code();
            }
            if (req.policyHeader().underwriter().contact() != null && req.policyHeader().underwriter().contact().email() != null) {
                contactEmail = req.policyHeader().underwriter().contact().email();
            }
        }

        Map<String, Object> underwritingSummary = new HashMap<>();
        underwritingSummary.put("approved", true);
        underwritingSummary.put("score", riskScore);
        underwritingSummary.put("tier", "PREMIUM");

        Map<String, Object> classificationSummary = new HashMap<>();
        classificationSummary.put("tier", "PREMIUM");
        classificationSummary.put("score", riskScore);

        Map<String, Object> classificationDetails = new HashMap<>();
        classificationDetails.put("score", riskScore);
        classificationDetails.put("band", "A");

        classificationSummary.put("details", classificationDetails);

        Map<String, Object> profileSummary = new HashMap<>();
        profileSummary.put("classification", classificationSummary);

        underwritingSummary.put("profile", profileSummary);

        Map<String, Object> underwriterSummary = new HashMap<>();
        underwriterSummary.put("code", underwriterCode);
        underwriterSummary.put("region", underwriterRegion);

        return new PolicyResponse(
            policyNumber,
            "APPROVED",
            EVALUATION_TIMESTAMP,
            EXPIRATION_TIMESTAMP,
            req.policyHeader() != null && req.policyHeader().effectiveDate() != null
                ? req.policyHeader().effectiveDate().toLocalDate()
                : DEFAULT_POLICY_EFFECTIVE_DATE,
            LocalDate.now(),
            LocalDateTime.now().minusMinutes(5).truncatedTo(ChronoUnit.SECONDS),
            LocalDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS),
            basePremium,
            totalPremium,
            discountAmount,
            finalPrice,
            approvedClauses.size(),
            riskScore,
            -50,
            riskScore,
            true,
            false,
            null,
            "550e8400-e29b-41d4-a716-446655440000",
            "https://portal.pathora.co.za/policies",
            "192.168.1.100",
            "Pathora Enterprise",
            "COMMERCIAL",
            "Approved With Condition",
            "   ",
            "",
            "2000",
            legalName,
            creditRating,
            underwriterRegion,
            contactEmail,
            approvedClauses,
            List.of("PROOF_OF_REGISTER", "AUDIT_REPORT", "TAX_CLEARANCE"),
            List.of("SENIOR_AUDITOR_SMITH"),
            tags,
            Collections.emptyList(),
            List.of("REF-101", "REF-102", "REF-103"),
            List.of("REF-999", "REF-888", "REF-777"),
            List.of("   ", ""),
            List.of("VALID", "  "),
            List.of("DOC1", "DOC2"),
            riskFlags,
            underwritingSummary,
            underwriterSummary,
            Collections.emptyMap()
        );
    }
}


