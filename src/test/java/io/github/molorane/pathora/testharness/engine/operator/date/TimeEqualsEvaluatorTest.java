package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TimeEqualsEvaluatorTest {

    private TimeEqualsEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new TimeEqualsEvaluator();
    }

    @Test
    @DisplayName("OPERATOR: returns TIME_EQUALS")
    void shouldReturnCorrectOperator() {
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.TIME_EQUALS);
    }

    @Test
    @DisplayName("PASS: time matches HH:mm:ss from datetime string")
    void shouldPassWhenTimeMatchesSecondsFromDateTime() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", "23:59:59", true));
    }

    @Test
    @DisplayName("PASS: time matches HH:mm from datetime string (ignoring seconds when expected is HH:mm)")
    void shouldPassWhenTimeMatchesMinutesFromDateTime() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", "23:59", true));
    }

    @Test
    @DisplayName("PASS: time matches HH:mm from time string")
    void shouldPassWhenTimeMatchesMinutesFromTime() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.time", "23:59:00", "23:59", true));
    }

    @Test
    @DisplayName("PASS: time matches HH:mm:ss from time string")
    void shouldPassWhenTimeMatchesSecondsFromTime() {
        assertThatNoException().isThrownBy(() ->
            evaluator.apply("$.time", "23:59:59", "23:59:59", true));
    }

    @Test
    @DisplayName("FAIL: hour/minute does not match")
    void shouldFailWhenHourMinuteDoesNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", "23:58", true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("TIME_EQUALS failed")
            .hasMessageContaining("Expected time: 23:58");
    }

    @Test
    @DisplayName("FAIL: seconds do not match when expected specifies seconds")
    void shouldFailWhenSecondsDoNotMatch() {
        assertThatThrownBy(() -> evaluator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", "23:59:00", true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("TIME_EQUALS failed")
            .hasMessageContaining("Expected time: 23:59:00");
    }

    @Test
    @DisplayName("FAIL: invalid expected time throws IllegalArgumentException")
    void shouldFailOnInvalidExpectedTime() {
        assertThatThrownBy(() -> evaluator.apply("$.time", "23:59:59", "invalid-time", true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot parse time");
    }
}

