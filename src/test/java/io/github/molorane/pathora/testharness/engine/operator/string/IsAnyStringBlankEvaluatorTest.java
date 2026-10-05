package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsAnyStringBlankEvaluatorTest {

    private IsAnyStringBlankEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsAnyStringBlankEvaluator();
    }

    @Test
    @DisplayName("PASS: single blank string")
    void shouldPassWhenSingleBlank() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "   ", null, true));
    }

    @Test
    @DisplayName("PASS: list containing at least one blank string")
    void shouldPassWhenListContainsBlank() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.items", List.of("apple", "  ", "cherry"), null, true));
    }

    @Test
    @DisplayName("FAIL: single non-blank string")
    void shouldFailWhenSingleNonBlank() {
        assertThatThrownBy(() -> operator.apply("$.text", "hello", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_ANY_STRING_BLANK failed");
    }

    @Test
    @DisplayName("FAIL: list with all non-blank strings")
    void shouldFailWhenAllNonBlank() {
        assertThatThrownBy(() ->
                operator.apply("$.items", List.of("apple", "banana", "cherry"), null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_ANY_STRING_BLANK failed");
    }
}

