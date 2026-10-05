package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class TimeBetweenEvaluatorTest {

    private TimeBetweenEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new TimeBetweenEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns TIME_BETWEEN")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.TIME_BETWEEN);
    }

    @Test
    @DisplayName("PASS: time is inclusively between min and max")
    void shouldPassWhenTimeIsBetween() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.timestamp", "2023-07-14T23:59:59", Map.of("min", "23:00:00", "max", "23:59:59"), true));
    }

    @Test
    @DisplayName("FAIL: time is outside bounds")
    void shouldFailWhenTimeIsOutsideBounds() {
        assertThatThrownBy(() -> evaluator.apply("$.timestamp", "2023-07-14T10:00:00", Map.of("min", "12:00:00", "max", "14:00:00"), true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("TIME_BETWEEN failed");
    }
}

