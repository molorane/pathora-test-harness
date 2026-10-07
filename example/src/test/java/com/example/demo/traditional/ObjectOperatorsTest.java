package com.example.demo.traditional;

import com.example.demo.dto.PolicyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ObjectOperatorsTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Object Emptiness & Partial Fields Test")
    void objectEmptinessAndPartialFieldsTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue(response.emptyMetadata().isEmpty(), "Empty metadata is an empty object"),
                () -> assertFalse(response.underwritingSummary().isEmpty(), "Underwriting summary is a non-empty object"),
                () -> assertEquals("PREMIUM", response.underwritingSummary().get("tier"), "Underwriting summary contains partial field tier=PREMIUM"),
                () -> assertEquals(820, ((Map<?, ?>) ((Map<?, ?>) ((Map<?, ?>) response.underwritingSummary().get("profile")).get("classification")).get("details")).get("score"), "Underwriting summary contains nested partial field profile.classification.details.score=820"),
                () -> assertFalse(response.underwritingSummary().containsKey("suspended") || response.underwritingSummary().containsKey("blacklisted"), "Underwriting summary does not contain blacklisted keys"),
                () -> assertTrue(response.basePremium() != response.discountAmount(), "Base premium does not equal discount amount"),
                () -> assertTrue(response.totalPremium() > response.finalPrice(), "Total premium is strictly greater than final discounted price"),
                () -> assertTrue(response.discountAmount() < response.basePremium(), "Discount amount is strictly less than base premium")
        );
    }
}

