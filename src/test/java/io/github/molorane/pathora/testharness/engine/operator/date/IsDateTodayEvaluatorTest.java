package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsDateTodayEvaluatorTest {

    private IsDateTodayEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsDateTodayEvaluator();
    }

    @Test
    @DisplayName("PASS: date is today")
    void shouldPassWhenDateIsToday() {
        String todayStr = LocalDate.now().toString();
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", todayStr, null, true));
    }

    @Test
    @DisplayName("FAIL: date is not today")
    void shouldFailWhenDateIsNotToday() {
        assertThatThrownBy(() -> operator.apply("$.date", "2000-01-01", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_TODAY failed");
    }
}

