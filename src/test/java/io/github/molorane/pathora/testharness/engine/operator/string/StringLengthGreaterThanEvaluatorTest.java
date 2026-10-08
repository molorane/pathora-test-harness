package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringLengthGreaterThanEvaluatorTest {

    private StringLengthGreaterThanEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringLengthGreaterThanEvaluator();
    }

    @Test
    @DisplayName("PASS: string length is strictly greater than expected")
    void shouldPassWhenLengthGreaterThan() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "ABCDE", 3, true));
    }

    @Test
    @DisplayName("FAIL: string length is less than or equal to expected")
    void shouldFailWhenLengthNotGreaterThan() {
        assertThatThrownBy(() -> operator.apply("$.code", "ABC", 3, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_LENGTH_GREATER_THAN failed");
    }
}

