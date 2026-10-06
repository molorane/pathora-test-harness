package com.example.demo.traditional;

import com.example.demo.dto.PolicyRequest;
import com.example.demo.dto.PolicyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.RecordComponent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PolicyRiskAssessmentTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Test 1: High Risk Score Parameter Mutation Test")
    void highRiskScoreParameterMutationTest() throws Exception {
        PolicyResponse response = evaluatePolicy(request -> new PolicyRequest(
                request.policyHeader(),
                new PolicyRequest.InsuredParty(
                        request.insuredParty().entityId(),
                        request.insuredParty().legalName(),
                        request.insuredParty().registrationNumber(),
                        new PolicyRequest.RiskMetrics(
                                request.insuredParty().riskMetrics().creditRating(),
                                900,
                                request.insuredParty().riskMetrics().priorClaimsCount(),
                                request.insuredParty().riskMetrics().flaggedRisks()
                        )
                ),
                new PolicyRequest.CoverageDetails(25000.00, request.coverageDetails().deductible(), request.coverageDetails().clauses(), request.coverageDetails().endorsements())
        ));

        assertAll(
                () -> assertTrue(response.riskScore() >= 900, "Risk score must be at least 900 after mutation"),
                () -> assertTrue(response.finalPrice() > 20000.0, "Final price must exceed 20000.0 with increased base premium"),
                () -> assertEquals(1, response.singlePrimaryAuditor().size(), "Primary auditor array must contain exactly one matching value"),
                () -> assertEquals("SENIOR_AUDITOR_SMITH", response.singlePrimaryAuditor().get(0), "Primary auditor array must contain exactly one matching value"),
                () -> assertTrue(response.underwriterRegion().endsWith("_SOUTH"), "Region code must end with _SOUTH"),
                () -> assertTrue(response.discountAmount() < 5000.0, "Discount amount must be less than 5000.0")
        );
    }

    @Test
    @DisplayName("Test 2: Date & Time Chronology Inspection")
    void dateTimeChronologyInspection() throws Exception {
        PolicyResponse response = evaluatePolicy();
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        assertAll(
                () -> assertTrue(response.expirationTimestamp().isAfter(now), "Expiration timestamp must be in the future relative to current time"),
                () -> assertTrue(response.policyHeaderEffectiveDate().isBefore(LocalDate.of(2030, 1, 1)), "Effective date must be before 2030"),
                () -> assertTrue(response.evaluationTimestamp().isAfter(LocalDateTime.of(2025, 1, 1, 0, 0)), "Evaluation timestamp must be after 2025"),
                () -> assertTrue(response.expirationTimestamp().isAfter(now) && !response.expirationTimestamp().isAfter(now.plusDays(365)), "Expiration timestamp must fall within next 365 days"),
                () -> assertFalse(response.policyHeaderEffectiveDate().isAfter(today), "Effective date must be in the past or present")
        );
    }

    @Test
    @DisplayName("Test 3: Policy Validity Duration Verification")
    void policyValidityDurationVerification() throws Exception {
        PolicyResponse response = evaluatePolicy();
        long days = ChronoUnit.DAYS.between(response.evaluationTimestamp(), response.expirationTimestamp());
        long months = ChronoUnit.MONTHS.between(response.evaluationTimestamp(), response.expirationTimestamp());
        long years = ChronoUnit.YEARS.between(response.evaluationTimestamp(), response.expirationTimestamp());

        assertAll(
                () -> assertEquals(365, days, "Duration between evaluation and expiration must equal 365 days"),
                () -> assertTrue(months > 11, "Duration between dates must exceed 11 months"),
                () -> assertTrue(years < 2, "Duration between dates must be less than 2 years"),
                () -> assertTrue(response.expirationTimestamp().isAfter(response.evaluationTimestamp().plusDays(360)), "Expiration must be after evaluation + 360 days"),
                () -> assertTrue(response.expirationTimestamp().isBefore(response.evaluationTimestamp().plusDays(400)), "Expiration must be before evaluation + 400 days")
        );
    }

    @Test
    @DisplayName("Test 4: Excluded Risk Flags & Value Constraints")
    void excludedRiskFlagsAndValueConstraints() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertFalse(response.riskFlags().containsAll(Set.of("FLOOD", "EARTHQUAKE", "VOLCANO")), "Risk flags must not contain all of FLOOD, EARTHQUAKE, VOLCANO"),
                () -> assertEquals(Set.of("REF-101", "REF-102", "REF-103"), Set.copyOf(response.uniqueReferenceCodes()), "Reference codes array must contain only specified values in any order"),
                () -> assertTrue(java.util.Arrays.stream(PolicyResponse.class.getRecordComponents()).map(RecordComponent::getName).noneMatch("nonExistentField"::equals), "nonExistentField must not exist in response"),
                () -> assertFalse(response.insuredPartyLegalName().isEmpty(), "Legal name must not be empty string"),
                () -> assertTrue(response.appliedClausesCount() >= 1, "Applied clauses count must be at least 1")
        );
    }

    @Test
    @DisplayName("Test 5: Comprehensive Logical & Structural Verification")
    void comprehensiveLogicalAndStructuralVerification() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue("APPROVED".equals(response.status()) && "Pathora Enterprise Solutions".equals(response.insuredPartyLegalName()), "Status is APPROVED AND legalName is Pathora Enterprise Solutions"),
                () -> assertTrue("AA+".equals(response.creditRating()) || "AAA".equals(response.creditRating()), "Credit rating is AA+ or AAA"),
                () -> assertFalse("SUSPENDED".equals(response.status()), "Status is NOT SUSPENDED"),
                () -> assertTrue("UW-NORTH-99".equals(response.underwriterSummary().get("code")) && "AFRICA_SOUTH".equals(response.underwriterSummary().get("region")), "Underwriter summary must contain code and region"),
                () -> assertEquals(14450.0, response.finalPrice(), 0.0001, "Final price must equal 14450.0")
        );
    }
}

