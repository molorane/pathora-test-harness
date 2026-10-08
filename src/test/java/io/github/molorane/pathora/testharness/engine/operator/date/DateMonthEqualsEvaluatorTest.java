package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DateMonthEqualsEvaluatorTest {

    private DateMonthEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DateMonthEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns DATE_MONTH_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.DATE_MONTH_EQUALS);
    }

    @Test
    @DisplayName("PASS: month matches with integer month number")
    void shouldPassWhenMonthMatchesInteger() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.date", "2025-06-13", 6, true));
    }

    @Test
    @DisplayName("PASS: month matches with string number")
    void shouldPassWhenMonthMatchesStringNumber() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.date", "2025-06-13", "06", true));
    }

    @Test
    @DisplayName("PASS: month matches with full uppercase month name")
    void shouldPassWhenMonthMatchesFullName() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.timestamp", "2025-06-13T10:30:00", "JUNE", true));
    }

    @Test
    @DisplayName("PASS: month matches with mixed case month name")
    void shouldPassWhenMonthMatchesMixedCaseName() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.timestamp", "2025-06-13T10:30:00", "June", true));
    }

    @Test
    @DisplayName("PASS: month matches with 3-letter abbreviation")
    void shouldPassWhenMonthMatchesAbbreviation() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.date", "2025-06-13", "Jun", true));
    }

    @Test
    @DisplayName("FAIL: month does not match")
    void shouldFailWhenMonthDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", "JULY", true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DATE_MONTH_EQUALS failed")
            .hasMessageContaining("Expected month: JULY")
            .hasMessageContaining("Actual month: JUNE");
    }

    @Test
    @DisplayName("FAIL: invalid month name throws IllegalArgumentException")
    void shouldFailOnInvalidMonthName() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", "INVALID_MONTH", true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid month value");
    }

    @Test
    @DisplayName("FAIL: invalid month number throws IllegalArgumentException")
    void shouldFailOnInvalidMonthNumber() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", 13, true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid month number");
    }
}

