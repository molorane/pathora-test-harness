package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import static org.assertj.core.api.Assertions.*;

class DateDayOfWeekEqualsEvaluatorTest {

    private DateDayOfWeekEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DateDayOfWeekEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns DATE_DAY_OF_WEEK_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.DATE_DAY_OF_WEEK_EQUALS);
    }

    @Test
    @DisplayName("PASS: day of week matches with full name (2025-06-13 is Friday)")
    void shouldPassWhenDayOfWeekMatchesFullName() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.date", "2025-06-13", "FRIDAY", true));
    }

    @Test
    @DisplayName("PASS: day of week matches with mixed case name")
    void shouldPassWhenDayOfWeekMatchesMixedCaseName() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.timestamp", "2025-06-13T10:30:00", "Friday", true));
    }

    @Test
    @DisplayName("PASS: day of week matches with abbreviation")
    void shouldPassWhenDayOfWeekMatchesAbbreviation() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.date", "2025-06-13", "Fri", true));
    }

    @Test
    @DisplayName("PASS: day of week matches with integer number (5 for Friday)")
    void shouldPassWhenDayOfWeekMatchesNumber() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.date", "2025-06-13", 5, true));
    }

    @Test
    @DisplayName("FAIL: day of week does not match")
    void shouldFailWhenDayOfWeekDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", "MONDAY", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_DAY_OF_WEEK_EQUALS failed")
                .hasMessageContaining("Expected day of week: MONDAY")
                .hasMessageContaining("Actual day of week: FRIDAY");
    }

    @Test
    @DisplayName("FAIL: invalid day of week number throws IllegalArgumentException")
    void shouldFailOnInvalidDayOfWeekNumber() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", 8, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid day of week number");
    }

    @Test
    @DisplayName("FAIL: invalid day of week string throws IllegalArgumentException")
    void shouldFailOnInvalidDayOfWeekString() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", "INVALID_DAY", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid day of week value");
    }
}

