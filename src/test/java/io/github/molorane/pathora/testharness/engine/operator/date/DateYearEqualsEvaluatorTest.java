package io.github.molorane.pathora.testharness.engine.operator.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import static org.assertj.core.api.Assertions.*;

class DateYearEqualsEvaluatorTest {

    private DateYearEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DateYearEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns DATE_YEAR_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.DATE_YEAR_EQUALS);
    }

    @Test
    @DisplayName("PASS: year matches with integer expected on ISO date")
    void shouldPassWhenYearMatchesIntegerOnDate() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.date", "2025-06-13", 2025, true));
    }

    @Test
    @DisplayName("PASS: year matches with string expected on ISO datetime")
    void shouldPassWhenYearMatchesStringOnDateTime() {
        assertThatNoException().isThrownBy(() ->
                evaluator.apply("$.timestamp", "2025-06-13T10:30:00", "2025", true));
    }

    @Test
    @DisplayName("FAIL: year does not match")
    void shouldFailWhenYearDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2024-06-13", 2025, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DATE_YEAR_EQUALS failed")
                .hasMessageContaining("Expected year: 2025")
                .hasMessageContaining("Actual year: 2024");
    }

    @Test
    @DisplayName("FAIL: invalid expected year throws IllegalArgumentException")
    void shouldFailOnInvalidExpectedYear() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "2025-06-13", "invalid-year", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid year value");
    }

    @Test
    @DisplayName("FAIL: unparseable actual date throws IllegalArgumentException")
    void shouldFailOnUnparseableDate() {
        assertThatThrownBy(() -> evaluator.apply("$.date", "not-a-date", 2025, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot parse date/datetime");
    }
}

