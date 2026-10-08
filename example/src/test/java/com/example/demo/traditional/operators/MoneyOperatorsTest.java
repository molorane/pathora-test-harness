package com.example.demo.traditional.operators;

import com.example.demo.dto.policy.PolicyRequest;
import com.example.demo.dto.policy.PolicyResponse;
import com.example.demo.traditional.TraditionalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MoneyOperatorsTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Money Comparison & Scale Test")
    void moneyComparisonAndScaleTest() throws Exception {
        PolicyResponse response = evaluatePolicy(request -> new PolicyRequest(
                request.policyHeader(),
                request.insuredParty(),
                new PolicyRequest.CoverageDetails(15000.00, request.coverageDetails().deductible(), request.coverageDetails().clauses(), request.coverageDetails().endorsements())
        ));

        assertAll(
                () -> assertEquals(15000.00, response.basePremium(), 0.0001, "Base premium matches exact monetary value"),
                () -> assertTrue(response.basePremium() >= 15000.00, "Base premium is greater than or equal to 15000.00"),
                () -> assertTrue(response.totalPremium() > 10000.00, "Total premium is greater than 10000.00"),
                () -> assertTrue(response.discountAmount() < 2000.00, "Discount amount is less than 2000.00"),
                () -> assertTrue(response.discountAmount() <= 1000.00, "Discount amount is less than or equal to 1000.00"),
                () -> assertTrue(response.finalPrice() >= 10000.00 && response.finalPrice() <= 20000.00, "Final price falls within expected monetary range"),
                () -> assertTrue(Math.abs(response.basePremium() - 15000.00) <= 50.00, "Base premium matches within 50.00 tolerance")
        );
    }
}


