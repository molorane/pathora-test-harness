package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsFutureDateTimeEvaluatorTest {

    private IsFutureDateTimeEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsFutureDateTimeEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "IS_FUTURE_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: future date is after now")
    void shouldPassWithFutureDate() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", "2099-12-31", null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "IS_FUTURE_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: future datetime is after now")
    void shouldPassWithFutureDatetime() {
        String future = LocalDateTime.now().plusHours(1)
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", future, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "IS_FUTURE_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: past date is not after now")
    void shouldFailWithPastDate() {
        assertThatThrownBy(() -> operator.apply("$.expiry", "2020-01-01", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_FUTURE_DATETIME failed");
    }
}

