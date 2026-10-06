package com.example.demo.traditional;

import com.example.demo.dto.LoanRequest;
import com.example.demo.dto.LoanResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ScalarOperatorsTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Scalar Number Classification Test")
    void scalarNumberClassificationTest() throws Exception {
        LoanResponse response = processLoan(request -> new LoanRequest(request.applicantId(), 50000.00, 750, request.termMonths()));

        assertAll(
                () -> assertTrue(Double.isFinite(response.approvedAmount()), "Approved amount must be a number"),
                () -> assertTrue(response.approvedAmount() > 0, "Approved amount must be strictly positive"),
                () -> assertTrue(response.interestRate() % 1 != 0, "Interest rate has fractional decimal component"),
                () -> assertTrue(Math.abs(response.interestRate() - 6.5) <= 0.1, "Interest rate should equal 6.5 within 0.1 tolerance"),
                () -> assertTrue(response.creditTierScore() == Math.rint(response.creditTierScore()), "Credit tier score must be an integer"),
                () -> assertTrue(response.riskPenalty() < 0, "Risk penalty must be a negative integer"),
                () -> assertTrue(response.eligible(), "Eligible flag must be true")
        );
    }

    @Test
    @DisplayName("Scalar Nullability & Tolerance Test")
    void scalarNullabilityAndToleranceTest() throws Exception {
        LoanResponse response = processLoan(request -> new LoanRequest(request.applicantId(), request.requestedAmount(), 500, request.termMonths()));

        assertAll(
                () -> assertNotNull(response.applicationId(), "Application ID must not be null"),
                () -> assertNull(response.coSignerId(), "Co-signer ID must be null"),
                () -> assertFalse(response.requiresManualReview(), "Requires manual review must be false"),
                () -> assertEquals(0.0, response.approvedAmount(), 0.0001, "Approved amount must be zero for rejected loan"),
                () -> assertEquals(0.0, response.interestRate(), 0.0001, "Interest rate must be zero for rejected loan"),
                () -> assertEquals("REJECTED", response.decisionStatus(), "Decision status must be REJECTED")
        );
    }
}

