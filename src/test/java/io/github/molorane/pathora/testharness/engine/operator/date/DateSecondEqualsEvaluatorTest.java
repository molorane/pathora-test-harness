package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DateSecondEqualsEvaluatorTest {

    private DateSecondEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DateSecondEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns DATE_SECOND_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.DATE_SECOND_EQUALS);
    }

    @Test
    @DisplayName("PASS: second matches integer on datetime")
    void shouldPassWhenSecondMatchesInteger() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", 59, true));
    }

    @Test
    @DisplayName("PASS: second matches string on time")
    void shouldPassWhenSecondMatchesString() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.time", "23:59:59", "59", true));
    }

    @Test
    @DisplayName("FAIL: second does not match")
    void shouldFailWhenSecondDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", 0, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DATE_SECOND_EQUALS failed")
            .hasMessageContaining("Expected second: 0")
            .hasMessageContaining("Actual second: 59");
    }

    @Test
    @DisplayName("FAIL: second out of range (0-59)")
    void shouldFailOnSecondOutOfRange() {
        assertThatThrownBy(() -> evaluator.apply("$.time", "23:59:59", 60, true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid second");
    }
}

