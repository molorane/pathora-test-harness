package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeAfterEvaluatorTest {

    private DateTimeAfterEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateTimeAfterEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_AFTER", "Value": "2026-12-31T23:59:59" }
     * ```
     */
    @Test
    @DisplayName("PASS: datetime after expected")
    void shouldPassWhenAfter() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.ts", "2027-01-01T00:00:00", "2026-12-31T23:59:59", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_AFTER", "Value": "2026-12-31T10:00:00" }
     * ```
     */
    @Test
    @DisplayName("PASS: same day, later time")
    void shouldPassWhenSameDayLaterTime() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.ts", "2026-12-31T23:00:00", "2026-12-31T10:00:00", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_AFTER", "Value": "2026-12-31T23:59:58" }
     * ```
     */
    @Test
    @DisplayName("PASS: one second after")
    void shouldPassWhenOneSecondAfter() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.ts", "2026-12-31T23:59:59", "2026-12-31T23:59:58", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_AFTER", "Value": "2026-12-31T10:00:00" }
     * ```
     */
    @Test
    @DisplayName("FAIL: same datetime")
    void shouldFailWhenEqual() {
        assertThatThrownBy(() -> operator.apply("$.ts", "2026-12-31T10:00:00", "2026-12-31T10:00:00", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_AFTER failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_AFTER", "Value": "2026-12-31T23:59:59" }
     * ```
     */
    @Test
    @DisplayName("FAIL: datetime before expected")
    void shouldFailWhenBefore() {
        assertThatThrownBy(() -> operator.apply("$.ts", "2025-06-15T10:00:00", "2026-12-31T23:59:59", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_AFTER failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_AFTER", "Value": "2026-12-31T10:00:00" }
     * ```
     */
    @Test
    @DisplayName("FAIL: invalid datetime format")
    void shouldFailWithInvalidFormat() {
        assertThatThrownBy(() -> operator.apply("$.ts", "not-a-datetime", "2026-12-31T10:00:00", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot parse datetime");
    }
}
