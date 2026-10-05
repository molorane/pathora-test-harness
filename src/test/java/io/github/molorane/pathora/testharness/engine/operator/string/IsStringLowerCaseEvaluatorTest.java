package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringLowerCaseEvaluatorTest {

    private IsStringLowerCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringLowerCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: lowercase string")
    void shouldPassWhenLowerCase() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "helloworld", null, true));
    }

    @Test
    @DisplayName("FAIL: contains uppercase")
    void shouldFailWhenNotLowerCase() {
        assertThatThrownBy(() -> operator.apply("$.text", "Hello", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_STRING_LOWER_CASE failed");
    }
}

