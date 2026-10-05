package io.github.molorane.pathora.testharness.engine.operator.datetime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeEqualsEvaluatorTest {

    private DateTimeEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateTimeEqualsEvaluator();
    }

    @Test
    @DisplayName("PASS: datetimes match exactly")
    void shouldPassWhenDateTimesMatch() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.timestamp", "2025-06-15T10:30:00", "2025-06-15T10:30:00", true));
    }

    @Test
    @DisplayName("FAIL: datetimes do not match")
    void shouldFailWhenDateTimesDoNotMatch() {
        assertThatThrownBy(() ->
                operator.apply("$.timestamp", "2025-06-15T10:30:00", "2025-06-15T10:30:01", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: subsecond difference without tolerance")
    void shouldFailWhenSubSecondDifference() {
        assertThatThrownBy(() ->
                operator.apply("$.timestamp", "2025-06-15T10:30:00.999", "2025-06-15T10:30:00", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_EQUALS failed");
    }
}

