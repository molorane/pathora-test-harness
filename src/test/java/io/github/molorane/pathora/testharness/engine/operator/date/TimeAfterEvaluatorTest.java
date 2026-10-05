package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import static org.assertj.core.api.Assertions.*;

class TimeAfterEvaluatorTest {

    private TimeAfterEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new TimeAfterEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns TIME_AFTER")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.TIME_AFTER);
    }

    @Test
    @DisplayName("PASS: actual time is strictly after expected time")
    void shouldPassWhenTimeIsAfter() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.timestamp", "2023-07-14T23:59:59", "23:00:00", true));
    }

    @Test
    @DisplayName("FAIL: actual time is equal to expected time")
    void shouldFailWhenTimeIsEqual() {
        assertThatThrownBy(() -> evaluator.apply("$.timestamp", "2023-07-14T23:59:59", "23:59:59", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("TIME_AFTER failed");
    }

    @Test
    @DisplayName("FAIL: actual time is before expected time")
    void shouldFailWhenTimeIsBefore() {
        assertThatThrownBy(() -> evaluator.apply("$.timestamp", "2023-07-14T10:00:00", "12:00:00", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("TIME_AFTER failed");
    }
}

