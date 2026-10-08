package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringNotBlankEvaluatorTest {

    private IsStringNotBlankEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringNotBlankEvaluator();
    }

    @Test
    @DisplayName("PASS: non-blank string")
    void shouldPassWhenNotBlank() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "hello", null, true));
    }

    @Test
    @DisplayName("FAIL: blank or whitespace string")
    void shouldFailWhenBlank() {
        assertThatThrownBy(() -> operator.apply("$.text", "   ", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_NOT_BLANK failed");

        assertThatThrownBy(() -> operator.apply("$.text", "", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_NOT_BLANK failed");
    }
}

