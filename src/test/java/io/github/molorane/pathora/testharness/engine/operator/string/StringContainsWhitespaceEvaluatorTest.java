package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringContainsWhitespaceEvaluatorTest {

    private StringContainsWhitespaceEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringContainsWhitespaceEvaluator();
    }

    @Test
    @DisplayName("PASS: string contains whitespace")
    void shouldPassWhenContainsWhitespace() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "Hello World", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "\tleading tab", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "trailing space ", null, true));
    }

    @Test
    @DisplayName("FAIL: string does not contain whitespace")
    void shouldFailWhenNoWhitespace() {
        assertThatThrownBy(() -> operator.apply("$.text", "HelloWorld", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_CONTAINS_WHITESPACE failed");
    }
}

