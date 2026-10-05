package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringAlphaEvaluatorTest {

    private IsStringAlphaEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringAlphaEvaluator();
    }

    @Test
    @DisplayName("PASS: letters only")
    void shouldPassWhenAlphaOnly() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "ValidCode", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "abc", null, true));
    }

    @Test
    @DisplayName("FAIL: contains digits")
    void shouldFailWhenContainsDigits() {
        assertThatThrownBy(() -> operator.apply("$.code", "Code123", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_STRING_ALPHA failed");
    }

    @Test
    @DisplayName("FAIL: contains spaces")
    void shouldFailWhenContainsSpaces() {
        assertThatThrownBy(() -> operator.apply("$.code", "Code Test", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_STRING_ALPHA failed");
    }
}

