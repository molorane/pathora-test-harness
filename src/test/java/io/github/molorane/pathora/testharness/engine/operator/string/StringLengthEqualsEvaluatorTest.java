package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringLengthEqualsEvaluatorTest {

    private StringLengthEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringLengthEqualsEvaluator();
    }

    @Test
    @DisplayName("PASS: string length equals expected")
    void shouldPassWhenLengthMatches() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "ABCDE", 5, true));
    }

    @Test
    @DisplayName("FAIL: string length mismatch")
    void shouldFailWhenLengthMismatch() {
        assertThatThrownBy(() -> operator.apply("$.code", "ABC", 5, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_LENGTH_EQUALS failed");
    }
}

