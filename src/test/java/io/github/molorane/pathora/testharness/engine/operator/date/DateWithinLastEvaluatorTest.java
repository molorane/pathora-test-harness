package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.TestJsonHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
     * { "JsonPath": "$.date", "Operator": "DATE_WITHIN_LAST", "Value": { "amount": 7, "unit": "DAYS" } }
     * ```
     */
    @Test
    @DisplayName("PASS: date within last 7 days")
    void shouldPassWithDateWithinDays() {
        String recent = LocalDate.now().minusDays(3).toString();
        Object value = TestJsonHelper.parse("""
            {
              "amount": 7,
              "unit": "DAYS"
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", recent, value, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "DATE_WITHIN_LAST", "Value": { "amount": 1, "unit": "DAYS" } }
     * ```
     */
    @Test
    @DisplayName("PASS: date is today")
    void shouldPassWhenToday() {
        String today = LocalDate.now().toString();
        Object value = TestJsonHelper.parse("""
            {
              "amount": 1,
              "unit": "DAYS"
            }
            """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", today, value, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "DATE_WITHIN_LAST", "Value": { "amount": 7, "unit": "DAYS" } }
     * ```
     */
    @Test
    @DisplayName("FAIL: date too old")
    void shouldFailWhenTooOld() {
        String old = LocalDate.now().minusDays(10).toString();
        Object value = TestJsonHelper.parse("""
            {
              "amount": 7,
              "unit": "DAYS"
            }
            """);
        assertThatThrownBy(() -> operator.apply("$.date", old, value, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DATE_WITHIN_LAST failed");
    }
}

