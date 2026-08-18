package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateWithinLastEvaluatorTest {

    private DateWithinLastEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateWithinLastEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.createdAt", "Operator": "DATE_WITHIN_LAST", "Value": value }
     * ```
     */
    @Test
    @DisplayName("PASS: datetime within last 24 hours")
    void shouldPassWhenWithinLast24Hours() {
        String recent = LocalDateTime.now().minusHours(5)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 24,
                  "unit": "HOURS"
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.createdAt", recent, value, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.createdAt", "Operator": "DATE_WITHIN_LAST", "Value": value }
     * ```
     */
    @Test
    @DisplayName("PASS: datetime just now")
    void shouldPassWhenJustNow() {
        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 1,
                  "unit": "HOURS"
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.createdAt", now, value, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.createdAt", "Operator": "DATE_WITHIN_LAST", "Value": value }
     * ```
     */
    @Test
    @DisplayName("PASS: date within last 7 days")
    void shouldPassWithDateWithinDays() {
        String recent = LocalDateTime.now().minusDays(3)
                .toLocalDate().toString();
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 7,
                  "unit": "DAYS"
                }
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.createdAt", recent, value, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.createdAt", "Operator": "DATE_WITHIN_LAST", "Value": value }
     * ```
     */
    @Test
    @DisplayName("FAIL: datetime too old")
    void shouldFailWhenTooOld() {
        String old = LocalDateTime.now().minusDays(10)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 24,
                  "unit": "HOURS"
                }
                """);
        assertThatThrownBy(() -> operator.apply("$.createdAt", old, value, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_WITHIN_LAST failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.createdAt", "Operator": "DATE_WITHIN_LAST", "Value": value }
     * ```
     */
    @Test
    @DisplayName("FAIL: invalid unit")
    void shouldFailWithInvalidUnit() {
        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Object value = TestJsonHelper.parse("""
                {
                  "amount": 1,
                  "unit": "INVALID"
                }
                """);
        assertThatThrownBy(() -> operator.apply("$.createdAt", now, value, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid duration unit");
    }
}
