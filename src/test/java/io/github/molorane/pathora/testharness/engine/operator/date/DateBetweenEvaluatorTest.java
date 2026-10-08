package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateBetweenEvaluatorTest {

    private DateBetweenEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateBetweenEvaluator();
    }

    @Test
    @DisplayName("PASS: date is within bounds")
    void shouldPassWhenDateWithinBounds() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.date", "2025-06-15", Map.of("min", "2025-01-01", "max", "2025-12-31"), true));
    }

    @Test
    @DisplayName("FAIL: date is outside bounds")
    void shouldFailWhenDateOutsideBounds() {
        assertThatThrownBy(() ->
            operator.apply("$.date", "2026-01-01", Map.of("min", "2025-01-01", "max", "2025-12-31"), true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DATE_BETWEEN failed");
    }
}

