package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringAlphaSpaceEvaluatorTest {

    private IsStringAlphaSpaceEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringAlphaSpaceEvaluator();
    }

    @Test
    @DisplayName("PASS: letters and spaces only")
    void shouldPassWhenAlphaSpace() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "John Doe", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "John", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "   ", null, true));
    }

    @Test
    @DisplayName("FAIL: contains digits")
    void shouldFailWhenContainsDigits() {
        assertThatThrownBy(() -> operator.apply("$.name", "John Doe 2", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_ALPHA_SPACE failed");
    }

    @Test
    @DisplayName("FAIL: contains special characters")
    void shouldFailWhenContainsSpecialChars() {
        assertThatThrownBy(() -> operator.apply("$.name", "John-Doe", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_ALPHA_SPACE failed");
    }
}

