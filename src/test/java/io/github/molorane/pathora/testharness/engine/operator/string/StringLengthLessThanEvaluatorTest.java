package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringLengthLessThanEvaluatorTest {

    private StringLengthLessThanEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringLengthLessThanEvaluator();
    }

    @Test
    @DisplayName("PASS: string length is strictly less than expected")
    void shouldPassWhenLengthLessThan() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "AB", 3, true));
    }

    @Test
    @DisplayName("FAIL: string length is greater than or equal to expected")
    void shouldFailWhenLengthNotLessThan() {
        assertThatThrownBy(() -> operator.apply("$.code", "ABC", 3, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_LENGTH_LESS_THAN failed");
    }
}

