package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DateHourEqualsEvaluatorTest {

    private DateHourEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DateHourEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns DATE_HOUR_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.DATE_HOUR_EQUALS);
    }

    @Test
    @DisplayName("PASS: hour matches integer on datetime")
    void shouldPassWhenHourMatchesInteger() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", 23, true));
    }

    @Test
    @DisplayName("PASS: hour matches string on time")
    void shouldPassWhenHourMatchesString() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.time", "23:59:59", "23", true));
    }

    @Test
    @DisplayName("FAIL: hour does not match")
    void shouldFailWhenHourDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", 22, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DATE_HOUR_EQUALS failed")
            .hasMessageContaining("Expected hour: 22")
            .hasMessageContaining("Actual hour: 23");
    }

    @Test
    @DisplayName("FAIL: hour out of range (0-23)")
    void shouldFailOnHourOutOfRange() {
        assertThatThrownBy(() -> evaluator.apply("$.time", "23:59:59", 24, true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid hour");
    }
}

