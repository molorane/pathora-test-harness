package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringBlankEvaluatorTest {

    private IsStringBlankEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringBlankEvaluator();
    }

    @Test
    @DisplayName("PASS: string is blank or empty or whitespace")
    void shouldPassWhenBlank() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "   ", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "\t\n", null, true));
    }

    @Test
    @DisplayName("FAIL: string has non-whitespace characters")
    void shouldFailWhenNotBlank() {
        assertThatThrownBy(() -> operator.apply("$.text", "hello", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_BLANK failed");
    }
}

