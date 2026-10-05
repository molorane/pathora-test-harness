package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import static org.assertj.core.api.Assertions.*;

class TimeBeforeEvaluatorTest {

    private TimeBeforeEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new TimeBeforeEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns TIME_BEFORE")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.TIME_BEFORE);
    }

    @Test
    @DisplayName("PASS: actual time is strictly before expected time")
    void shouldPassWhenTimeIsBefore() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.timestamp", "2023-07-14T10:30:00", "12:00:00", true));
    }

    @Test
    @DisplayName("FAIL: actual time is equal to expected time")
    void shouldFailWhenTimeIsEqual() {
        assertThatThrownBy(() -> evaluator.apply("$.timestamp", "2023-07-14T12:00:00", "12:00:00", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("TIME_BEFORE failed");
    }

    @Test
    @DisplayName("FAIL: actual time is after expected time")
    void shouldFailWhenTimeIsAfter() {
        assertThatThrownBy(() -> evaluator.apply("$.timestamp", "2023-07-14T14:00:00", "12:00:00", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("TIME_BEFORE failed");
    }
}

