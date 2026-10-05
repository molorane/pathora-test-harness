package com.example.demo.executor;

import com.example.demo.dto.PolicyRequest;
import com.example.demo.dto.PolicyResponse;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class ComplexPolicyExecutor implements EntryPointExecutor<PolicyRequest, PolicyResponse> {

    @Override
    public String getEntryPointName() {
        return "policy-evaluation-service";
    }

    @Override
    public Class<PolicyRequest> getRequestType() {
        return PolicyRequest.class;
    }

    @Override
    public PolicyResponse execute(PolicyRequest req) {
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

        Map<String, Object> underwriterSummary = new HashMap<>();
        underwriterSummary.put("code", underwriterCode);
        underwriterSummary.put("region", underwriterRegion);

        return new PolicyResponse(
                policyNumber,
                "APPROVED",
                "2026-09-15T12:30:45",
                "2027-09-15T12:30:45",
                "2026-01-01",
                LocalDate.now().toString(),
                LocalDateTime.now().minusMinutes(5).truncatedTo(ChronoUnit.SECONDS).toString(),
                LocalDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS).toString(),
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


