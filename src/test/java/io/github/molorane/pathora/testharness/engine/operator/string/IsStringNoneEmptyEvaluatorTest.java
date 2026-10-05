package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringNoneEmptyEvaluatorTest {

    private IsStringNoneEmptyEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringNoneEmptyEvaluator();
    }

    @Test
    @DisplayName("PASS: single non-empty string")
    void shouldPassWhenSingleNonEmpty() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "hello", null, true));
    }

    @Test
    @DisplayName("PASS: list of non-empty strings")
    void shouldPassWhenListOfNonEmpty() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.items", List.of("apple", "banana", "cherry"), null, true));
    }

    @Test
    @DisplayName("FAIL: single empty string")
    void shouldFailWhenSingleEmpty() {
        assertThatThrownBy(() -> operator.apply("$.text", "", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_STRING_NONE_EMPTY failed");
    }

    @Test
    @DisplayName("FAIL: list containing an empty string")
    void shouldFailWhenListContainsEmpty() {
        assertThatThrownBy(() ->
                operator.apply("$.items", List.of("apple", "", "cherry"), null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_STRING_NONE_EMPTY failed");
    }
}

