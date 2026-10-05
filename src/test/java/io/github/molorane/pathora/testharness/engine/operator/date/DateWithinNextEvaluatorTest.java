package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.engine.operator.TestJsonHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateWithinNextEvaluatorTest {

    private DateWithinNextEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateWithinNextEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_WITHIN_NEXT", "Value": { "amount": 7, "unit": "DAYS" } }
     * ```
     */
    @Test
    @DisplayName("PASS: date within next 7 days")
    void shouldPassWithDateWithinDays() {
        String upcoming = LocalDate.now().plusDays(3).toString();
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", upcoming, value, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_WITHIN_NEXT", "Value": { "amount": 7, "unit": "DAYS" } }
     * ```
     */
    @Test
    @DisplayName("PASS: date is today")
    void shouldPassWhenToday() {
        String today = LocalDate.now().toString();
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", today, value, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_WITHIN_NEXT", "Value": { "amount": 7, "unit": "DAYS" } }
     * ```
     */
    @Test
    @DisplayName("FAIL: date too far in future")
    void shouldFailWhenTooFarInFuture() {
        String farFuture = LocalDate.now().plusDays(30).toString();
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatThrownBy(() -> operator.apply("$.expiry", farFuture, value, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_WITHIN_NEXT failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_WITHIN_NEXT", "Value": { "amount": 7, "unit": "DAYS" } }
     * ```
     */
    @Test
    @DisplayName("FAIL: date in the past")
    void shouldFailWhenInPast() {
        String past = LocalDate.now().minusDays(1).toString();
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatThrownBy(() -> operator.apply("$.expiry", past, value, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_WITHIN_NEXT failed");
    }
}

