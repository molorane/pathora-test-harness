package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DateDayEqualsEvaluatorTest {

    private DateDayEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DateDayEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns DATE_DAY_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.DATE_DAY_EQUALS);
    }

    @Test
    @DisplayName("PASS: day matches with integer expected on ISO date")
    void shouldPassWhenDayMatchesIntegerOnDate() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.date", "2025-06-13", 13, true));
    }

    @Test
    @DisplayName("PASS: day matches with string expected on ISO datetime")
    void shouldPassWhenDayMatchesStringOnDateTime() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.timestamp", "2025-06-13T10:30:00", "13", true));
    }

    @Test
    @DisplayName("FAIL: day does not match")
    void shouldFailWhenDayDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", 14, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DATE_DAY_EQUALS failed")
            .hasMessageContaining("Expected day: 14")
            .hasMessageContaining("Actual day: 13");
    }

    @Test
    @DisplayName("FAIL: invalid day out of range throws IllegalArgumentException")
    void shouldFailOnDayOutOfRange() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", 32, true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid day of month");
    }

    @Test
    @DisplayName("FAIL: invalid non-numeric day throws IllegalArgumentException")
    void shouldFailOnNonNumericDay() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", "abc", true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid day value");
    }
}

