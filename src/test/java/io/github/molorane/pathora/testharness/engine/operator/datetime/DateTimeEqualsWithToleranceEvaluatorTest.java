package io.github.molorane.pathora.testharness.engine.operator.datetime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.engine.operator.TestJsonHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeEqualsWithToleranceEvaluatorTest {

    private DateTimeEqualsWithToleranceEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateTimeEqualsWithToleranceEvaluator();
    }

    @Test
    @DisplayName("PASS: datetimes match within seconds tolerance")
    void shouldPassWhenWithinTolerance() {
        Object value = TestJsonHelper.parse("""
                {
                  "expected": "2023-07-14T23:59:59",
                  "tolerance": 1,
                  "unit": "SECONDS"
                }
                """);
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.timestamp", "2023-07-14T23:59:59.999", value, true));
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.timestamp", "2023-07-14T23:59:58", value, true));
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.timestamp", "2023-07-15T00:00:00", value, true));
    }

    @Test
    @DisplayName("PASS: datetimes match within minutes tolerance using 'value' key")
    void shouldPassWithMinutesToleranceAndValueKey() {
        Object value = TestJsonHelper.parse("""
                {
                  "value": "2023-07-14T23:59:00",
                  "tolerance": 5,
                  "unit": "MINUTES"
                }
                """);
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.timestamp", "2023-07-14T23:55:00", value, true));
    }

    @Test
    @DisplayName("FAIL: datetimes exceed tolerance window")
    void shouldFailWhenExceedsTolerance() {
        Object value = TestJsonHelper.parse("""
                {
                  "expected": "2023-07-14T23:59:59",
                  "tolerance": 1,
                  "unit": "SECONDS"
                }
                """);
        assertThatThrownBy(() ->
                operator.apply("$.timestamp", "2023-07-14T23:59:57", value, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_EQUALS_WITH_TOLERANCE failed");
    }

    @Test
    @DisplayName("FAIL: missing tolerance configuration")
    void shouldFailWhenMissingTolerance() {
        Object value = TestJsonHelper.parse("""
                {
                  "expected": "2023-07-14T23:59:59"
                }
                """);
        assertThatThrownBy(() ->
                operator.apply("$.timestamp", "2023-07-14T23:59:59", value, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("requires 'expected' (or 'value') and 'tolerance'");
    }
}

