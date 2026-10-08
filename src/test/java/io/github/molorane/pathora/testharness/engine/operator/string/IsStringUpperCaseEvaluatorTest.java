package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringUpperCaseEvaluatorTest {

    private IsStringUpperCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringUpperCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: uppercase string")
    void shouldPassWhenUpperCase() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "HELLOWORLD", null, true));
    }

    @Test
    @DisplayName("FAIL: contains lowercase")
    void shouldFailWhenNotUpperCase() {
        assertThatThrownBy(() -> operator.apply("$.text", "Hello", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_UPPER_CASE failed");
    }
}

