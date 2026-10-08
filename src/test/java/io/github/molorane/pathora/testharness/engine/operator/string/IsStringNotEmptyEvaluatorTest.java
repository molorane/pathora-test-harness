package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringNotEmptyEvaluatorTest {

    private IsStringNotEmptyEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringNotEmptyEvaluator();
    }

    @Test
    @DisplayName("PASS: non-empty string (even spaces)")
    void shouldPassWhenNotEmpty() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "hello", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "   ", null, true));
    }

    @Test
    @DisplayName("FAIL: empty string or null")
    void shouldFailWhenEmpty() {
        assertThatThrownBy(() -> operator.apply("$.text", "", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_NOT_EMPTY failed");
    }
}

