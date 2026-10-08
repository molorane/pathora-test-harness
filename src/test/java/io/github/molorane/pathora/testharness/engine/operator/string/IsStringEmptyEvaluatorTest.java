package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringEmptyEvaluatorTest {

    private IsStringEmptyEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringEmptyEvaluator();
    }

    @Test
    @DisplayName("PASS: string is empty")
    void shouldPassWhenEmpty() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "", null, true));
    }

    @Test
    @DisplayName("FAIL: whitespace string is not empty")
    void shouldFailWhenWhitespace() {
        assertThatThrownBy(() -> operator.apply("$.text", "   ", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_EMPTY failed");
    }

    @Test
    @DisplayName("FAIL: non-empty string")
    void shouldFailWhenNonEmpty() {
        assertThatThrownBy(() -> operator.apply("$.text", "hello", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_EMPTY failed");
    }
}

