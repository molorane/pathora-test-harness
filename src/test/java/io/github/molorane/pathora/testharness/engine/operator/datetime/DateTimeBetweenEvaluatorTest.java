package io.github.molorane.pathora.testharness.engine.operator.datetime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeBetweenEvaluatorTest {

    private DateTimeBetweenEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateTimeBetweenEvaluator();
    }

    @Test
    @DisplayName("PASS: datetime is within bounds")
    void shouldPassWhenDateTimeWithinBounds() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.timestamp", "2025-06-15T12:00:00",
                        Map.of("min", "2025-01-01T00:00:00", "max", "2025-12-31T23:59:59"), true));
    }

    @Test
    @DisplayName("FAIL: datetime is outside bounds")
    void shouldFailWhenDateTimeOutsideBounds() {
        assertThatThrownBy(() ->
                operator.apply("$.timestamp", "2026-01-01T00:00:00",
                        Map.of("min", "2025-01-01T00:00:00", "max", "2025-12-31T23:59:59"), true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATETIME_BETWEEN failed");
    }
}

