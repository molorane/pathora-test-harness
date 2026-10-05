package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsFutureDateEvaluatorTest {

    private IsFutureDateEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsFutureDateEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "IS_FUTURE_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: future date is after today")
    void shouldPassWithFutureDate() {
        String futureDate = LocalDate.now().plusDays(1).toString();
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", futureDate, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", "2099-12-31", null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "IS_FUTURE_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: past date is not after today")
    void shouldFailWithPastDate() {
        assertThatThrownBy(() -> operator.apply("$.date", "2020-01-01", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_FUTURE_DATE failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.date", "Operator": "IS_FUTURE_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: today is not in the future")
    void shouldFailWithToday() {
        String today = LocalDate.now().toString();
        assertThatThrownBy(() -> operator.apply("$.date", today, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_FUTURE_DATE failed");
    }
}

