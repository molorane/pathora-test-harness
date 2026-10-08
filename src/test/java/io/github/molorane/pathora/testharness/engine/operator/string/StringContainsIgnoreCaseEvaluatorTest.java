package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringContainsIgnoreCaseEvaluatorTest {

    private StringContainsIgnoreCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringContainsIgnoreCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: string contains substring ignoring case")
    void shouldPassWhenContainsSubstringIgnoreCase() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.message", "Hello World", "world", true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.message", "Hello World", "WORLD", true));
    }

    @Test
    @DisplayName("FAIL: string does not contain substring")
    void shouldFailWhenNotContains() {
        assertThatThrownBy(() -> operator.apply("$.message", "Hello World", "Universe", true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_CONTAINS_IGNORE_CASE failed");
    }
}

