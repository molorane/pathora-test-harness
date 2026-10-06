package com.example.demo.traditional;

import com.example.demo.dto.PolicyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PolicyEvaluationTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Test 1: Policy Header & Underwriter Verification")
    void policyHeaderAndUnderwriterVerification() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue(response.policyNumber().startsWith("POL-"), "Policy number must start with POL- prefix"),
                () -> assertTrue(isValidEmail(response.primaryContactEmail()), "Contact email must be valid email format"),
                () -> assertFalse("REJECTED".equals(response.status()), "Status must not be REJECTED"),
                () -> assertTrue(response.underwritingSummary().keySet().containsAll(Set.of("approved", "score", "tier")), "Summary map must contain required key names"),
                () -> assertNotNull(response.evaluationTimestamp(), "Evaluation timestamp path must exist in response")
        );
    }

    @Test
    @DisplayName("Test 2: Premium & Financial Bounds Verification")
    void premiumAndFinancialBoundsVerification() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue(response.totalPremium() > 10000.0, "Total premium must exceed 10000.0"),
                () -> assertTrue(response.finalPrice() >= 10000.0 && response.finalPrice() <= 20000.0, "Final price must be between 10000.0 and 20000.0"),
                () -> assertTrue(response.discountAmount() <= 2000.0, "Discount amount must not exceed 2000.0"),
                () -> assertTrue(Set.of("AFRICA_SOUTH", "AFRICA_NORTH", "EUROPE").contains(response.underwriterRegion()), "Region must be in allowed list"),
                () -> assertEquals(response.riskScore(), response.insuredRiskScore(), "riskScore must equal insuredRiskScore")
        );
    }

    @Test
    @DisplayName("Test 3: Clause Array & Requirements Inspection")
    void clauseArrayAndRequirementsInspection() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertEquals(2, response.approvedClauses().size(), "Approved clauses array must have exact size 2"),
                () -> assertTrue(response.requiredDocs().contains("AUDIT_REPORT"), "Required docs must contain AUDIT_REPORT"),
                () -> assertTrue(response.approvedTags().containsAll(Set.of("PROPERTY", "CYBER")), "Approved tags must contain all specified tags"),
                () -> assertEquals(response.uniqueReferenceCodes().size(), response.uniqueReferenceCodes().stream().distinct().count(), "Reference codes must contain no duplicates"),
                () -> assertTrue(response.emptyExclusions().isEmpty(), "Exclusions list must be empty")
        );
    }

    @Test
    @DisplayName("Test 4: Object Composition & Risk Summary Validation")
    void objectCompositionAndRiskSummaryValidation() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue("PREMIUM".equals(response.underwritingSummary().get("tier")) && Boolean.TRUE.equals(response.underwritingSummary().get("approved")), "Summary object must contain specified key-value fields"),
                () -> assertTrue("PREMIUM".equals(response.underwritingSummary().get("tier")) && Integer.valueOf(820).equals(response.underwritingSummary().get("score")), "Summary object must contain fields ignoring null entries"),
                () -> assertTrue(response.approvedClauses().stream().anyMatch(clause -> "CLS-01".equals(clause.clauseId()) && "APPROVED".equals(clause.status())), "Approved clauses array must contain object matching fields"),
                () -> assertTrue(response.riskFlags().stream().noneMatch(flag -> Set.of("BANKRUPTCY", "FRAUD").contains(flag)), "Risk flags must not contain BANKRUPTCY or FRAUD"),
                () -> assertTrue(response.requiredDocs().stream().anyMatch(doc -> Set.of("TAX_CLEARANCE", "PASSPORT").contains(doc)), "Required docs must contain at least TAX_CLEARANCE or PASSPORT")
        );
    }

    @Test
    @DisplayName("Test 5: Logical Composition Rules Verification")
    void logicalCompositionRulesVerification() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue("APPROVED".equals(response.status()) && response.riskScore() > 800, "Status must be APPROVED AND riskScore > 800"),
                () -> assertTrue("AFRICA_SOUTH".equals(response.underwriterRegion()) || "EUROPE".equals(response.underwriterRegion()), "Region must be AFRICA_SOUTH or EUROPE"),
                () -> assertFalse("CANCELLED".equals(response.status()), "Status must NOT be CANCELLED"),
                () -> assertTrue(response.approvedClauses().stream().allMatch(clause -> clause.limit() > 0), "All approved clause limits must be greater than 0"),
                () -> assertFalse(Set.of("REJECTED", "CANCELLED").contains(response.status()), "Status must not be in excluded list")
        );
    }
}

