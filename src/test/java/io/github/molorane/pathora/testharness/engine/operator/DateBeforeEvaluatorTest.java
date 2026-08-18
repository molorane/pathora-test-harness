package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateBeforeEvaluatorTest {

    private DateBeforeEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateBeforeEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_BEFORE", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("PASS: date before expected")
    void shouldPassWhenBefore() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", "2025-06-15", "2026-12-31", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_BEFORE", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("PASS: one day before")
    void shouldPassWhenOneDayBefore() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", "2026-12-30", "2026-12-31", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_BEFORE", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("FAIL: same date")
    void shouldFailWhenEqual() {
        assertThatThrownBy(() -> operator.apply("$.expiry", "2026-12-31", "2026-12-31", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_BEFORE failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_BEFORE", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("FAIL: date after expected")
    void shouldFailWhenAfter() {
        assertThatThrownBy(() -> operator.apply("$.expiry", "2027-01-01", "2026-12-31", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_BEFORE failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "DATE_BEFORE", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("FAIL: invalid date format")
    void shouldFailWithInvalidFormat() {
        assertThatThrownBy(() -> operator.apply("$.expiry", "31/12/2026", "2026-12-31", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot parse date");
    }
}
