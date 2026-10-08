package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringAlphaNumericEvaluatorTest {

    private IsStringAlphaNumericEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringAlphaNumericEvaluator();
    }

    @Test
    @DisplayName("PASS: alphanumeric string")
    void shouldPassWhenAlphanumeric() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "User123", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "12345", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "abcde", null, true));
    }

    @Test
    @DisplayName("FAIL: contains special characters")
    void shouldFailWhenContainsSpecialChars() {
        assertThatThrownBy(() -> operator.apply("$.code", "User_123", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_ALPHA_NUMERIC failed");
    }

    @Test
    @DisplayName("FAIL: contains whitespace")
    void shouldFailWhenContainsWhitespace() {
        assertThatThrownBy(() -> operator.apply("$.code", "User 123", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_ALPHA_NUMERIC failed");
    }
}

