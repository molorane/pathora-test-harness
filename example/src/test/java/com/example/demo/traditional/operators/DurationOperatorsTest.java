package com.example.demo.traditional.operators;

import com.example.demo.dto.policy.PolicyResponse;
import com.example.demo.traditional.TraditionalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DurationOperatorsTest extends TraditionalTestSupport {

    @Test
    @DisplayName("Duration Calculation & Range Bounds Test")
    void durationCalculationAndRangeBoundsTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        long days = ChronoUnit.DAYS.between(response.evaluationTimestamp(), response.expirationTimestamp());
        long months = ChronoUnit.MONTHS.between(response.evaluationTimestamp(), response.expirationTimestamp());
        long years = ChronoUnit.YEARS.between(response.evaluationTimestamp(), response.expirationTimestamp());

        assertAll(
                () -> assertTrue(days == 365, "Duration between evaluation and expiration is exactly 365 days"),
                () -> assertTrue(days >= 300 && days <= 400, "Duration in days falls between 300 and 400"),
                () -> assertTrue(months > 10, "Duration between dates exceeds 10 months"),
                () -> assertTrue(years < 2, "Duration between dates is less than 2 years")
        );
    }

    @Test
    @DisplayName("Relative Date Duration Offset Test")
    void relativeDateDurationOffsetTest() throws Exception {
        PolicyResponse response = evaluatePolicy();

        assertAll(
                () -> assertTrue(response.expirationTimestamp().isAfter(response.evaluationTimestamp().plusDays(350)), "Expiration date is after evaluation timestamp + 350 days"),
                () -> assertTrue(response.expirationTimestamp().isBefore(response.evaluationTimestamp().plusDays(380)), "Expiration date is before evaluation timestamp + 380 days")
        );
    }
}


