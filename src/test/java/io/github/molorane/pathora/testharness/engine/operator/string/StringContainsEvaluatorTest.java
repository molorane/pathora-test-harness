package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringContainsEvaluatorTest {

    private StringContainsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringContainsEvaluator();
    }

    @Test
    @DisplayName("PASS: string contains substring")
    void shouldPassWhenContainsSubstring() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.message", "Hello World", "World", true));
    }

    @Test
    @DisplayName("PASS: string contains exact match")
    void shouldPassWhenExactMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "TEST", "TEST", true));
    }

    @Test
    @DisplayName("FAIL: string does not contain substring")
    void shouldFailWhenNotContains() {
        assertThatThrownBy(() -> operator.apply("$.message", "Hello World", "Universe", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_CONTAINS failed");
    }

    @Test
    @DisplayName("FAIL: case sensitive mismatch")
    void shouldFailWhenCaseMismatch() {
        assertThatThrownBy(() -> operator.apply("$.message", "Hello World", "world", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_CONTAINS failed");
    }
}

