package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringMixedCaseEvaluatorTest {

    private IsStringMixedCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringMixedCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: mixed case string")
    void shouldPassWhenMixedCase() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "Hello", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "hElLo", null, true));
    }

    @Test
    @DisplayName("FAIL: all upper case")
    void shouldFailWhenAllUpper() {
        assertThatThrownBy(() -> operator.apply("$.text", "HELLO", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_STRING_MIXED_CASE failed");
    }

    @Test
    @DisplayName("FAIL: all lower case")
    void shouldFailWhenAllLower() {
        assertThatThrownBy(() -> operator.apply("$.text", "hello", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_STRING_MIXED_CASE failed");
    }
}

