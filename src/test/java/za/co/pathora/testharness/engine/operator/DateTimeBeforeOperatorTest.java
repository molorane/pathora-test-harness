package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeBeforeOperatorTest {

    private DateTimeBeforeOperator operator;

    @BeforeEach
    void setUp() {
        operator = new DateTimeBeforeOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_BEFORE", "Value": "2026-12-31T23:59:59" }
     * ```
     */
    @Test
    @DisplayName("PASS: datetime before expected")
    void shouldPassWhenBefore() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.ts", "2025-06-15T10:30:00", "2026-12-31T23:59:59", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_BEFORE", "Value": "2026-12-31T23:00:00" }
     * ```
     */
    @Test
    @DisplayName("PASS: same day, earlier time")
    void shouldPassWhenSameDayEarlierTime() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.ts", "2026-12-31T10:00:00", "2026-12-31T23:00:00", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_BEFORE", "Value": "2026-12-31T23:59:59" }
     * ```
     */
    @Test
    @DisplayName("PASS: one second before")
    void shouldPassWhenOneSecondBefore() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.ts", "2026-12-31T23:59:58", "2026-12-31T23:59:59", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_BEFORE", "Value": "2026-12-31T10:00:00" }
     * ```
     */
    @Test
    @DisplayName("FAIL: same datetime")
    void shouldFailWhenEqual() {
        assertThatThrownBy(() -> operator.apply("$.ts", "2026-12-31T10:00:00", "2026-12-31T10:00:00", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_BEFORE failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_BEFORE", "Value": "2026-12-31T23:59:59" }
     * ```
     */
    @Test
    @DisplayName("FAIL: datetime after expected")
    void shouldFailWhenAfter() {
        assertThatThrownBy(() -> operator.apply("$.ts", "2027-01-01T00:00:00", "2026-12-31T23:59:59", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_BEFORE failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.ts", "Operator": "DATE_TIME_BEFORE", "Value": "2026-12-31T10:00:00" }
     * ```
     */
    @Test
    @DisplayName("FAIL: invalid datetime format")
    void shouldFailWithInvalidFormat() {
        assertThatThrownBy(() -> operator.apply("$.ts", "2026-12-31", "2026-12-31T10:00:00", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot parse datetime");
    }
}
