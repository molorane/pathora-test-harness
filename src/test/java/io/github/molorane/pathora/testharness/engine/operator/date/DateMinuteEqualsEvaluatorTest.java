package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import static org.assertj.core.api.Assertions.*;

class DateMinuteEqualsEvaluatorTest {

    private DateMinuteEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DateMinuteEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns DATE_MINUTE_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.DATE_MINUTE_EQUALS);
    }

    @Test
    @DisplayName("PASS: minute matches integer on datetime")
    void shouldPassWhenMinuteMatchesInteger() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", 59, true));
    }

    @Test
    @DisplayName("PASS: minute matches string on time")
    void shouldPassWhenMinuteMatchesString() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.time", "23:59:59", "59", true));
    }

    @Test
    @DisplayName("FAIL: minute does not match")
    void shouldFailWhenMinuteDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", 30, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_MINUTE_EQUALS failed")
                .hasMessageContaining("Expected minute: 30")
                .hasMessageContaining("Actual minute: 59");
    }

    @Test
    @DisplayName("FAIL: minute out of range (0-59)")
    void shouldFailOnMinuteOutOfRange() {
        assertThatThrownBy(() -> evaluator.apply("$.time", "23:59:59", 60, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid minute");
    }
}

