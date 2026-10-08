package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateEqualsEvaluatorTest {

    private DateEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DateEqualsEvaluator();
    }

    @Test
    @DisplayName("PASS: dates match")
    void shouldPassWhenDatesMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.date", "2025-06-15", "2025-06-15", true));
    }

    @Test
    @DisplayName("PASS: extracts and matches date part from datetime actual value")
    void shouldPassWhenExtractingDateFromDateTime() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.outputData.processedDate", "2023-07-14T23:59:59", "2023-07-14", true));
    }

    @Test
    @DisplayName("FAIL: dates do not match")
    void shouldFailWhenDatesDoNotMatch() {
        assertThatThrownBy(() -> operator.apply("$.date", "2025-06-15", "2025-06-16", true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DATE_EQUALS failed");
    }
}

