package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateAfterEvaluatorTest {

    private DateAfterEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateAfterEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.start", "Operator": "DATE_AFTER", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("PASS: date after expected")
    void shouldPassWhenAfter() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.start", "2027-01-01", "2026-12-31", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.start", "Operator": "DATE_AFTER", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("PASS: one day after")
    void shouldPassWhenOneDayAfter() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.start", "2027-01-01", "2026-12-31", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.start", "Operator": "DATE_AFTER", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("FAIL: same date")
    void shouldFailWhenEqual() {
        assertThatThrownBy(() -> operator.apply("$.start", "2026-12-31", "2026-12-31", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_AFTER failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.start", "Operator": "DATE_AFTER", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("FAIL: date before expected")
    void shouldFailWhenBefore() {
        assertThatThrownBy(() -> operator.apply("$.start", "2025-06-15", "2026-12-31", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_AFTER failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.start", "Operator": "DATE_AFTER", "Value": "2026-12-31" }
     * ```
     */
    @Test
    @DisplayName("FAIL: invalid date format")
    void shouldFailWithInvalidFormat() {
        assertThatThrownBy(() -> operator.apply("$.start", "not-a-date", "2026-12-31", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot parse date");
    }
}
