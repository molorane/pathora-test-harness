package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsPastDateTimeEvaluatorTest {

    private IsPastDateTimeEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsPastDateTimeEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "IS_PAST_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: past date is before now")
    void shouldPassWithPastDate() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", "2020-01-01", null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "IS_PAST_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: past datetime is before now")
    void shouldPassWithPastDatetime() {
        String past = LocalDateTime.now().minusHours(1)
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertThatNoException().isThrownBy(() -> operator.apply("$.expiry", past, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.expiry", "Operator": "IS_PAST_DATE", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: future date is not before now")
    void shouldFailWithFutureDate() {
        assertThatThrownBy(() -> operator.apply("$.expiry", "2099-12-31", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_PAST_DATETIME failed");
    }
}

