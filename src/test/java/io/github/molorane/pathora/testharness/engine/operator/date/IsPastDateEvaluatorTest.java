package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsPastDateEvaluatorTest {

    private IsPastDateEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsPastDateEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "IS_PAST_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: past date is before today")
    void shouldPassWithPastDate() {
        String pastDate = LocalDate.now().minusDays(1).toString();
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", pastDate, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", "2020-01-01", null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "IS_PAST_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: future date is not before today")
    void shouldFailWithFutureDate() {
        assertThatThrownBy(() -> operator.apply("$.date", "2099-12-31", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_PAST_DATE failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "IS_PAST_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: today is not in the past")
    void shouldFailWithToday() {
        String today = LocalDate.now().toString();
        assertThatThrownBy(() -> operator.apply("$.date", today, null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_PAST_DATE failed");
    }
}

