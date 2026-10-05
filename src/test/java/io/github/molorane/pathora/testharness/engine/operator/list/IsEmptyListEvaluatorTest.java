package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsEmptyListEvaluatorTest {

    private IsEmptyListEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsEmptyListEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "IS_EMPTY_LIST", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: empty array")
    void shouldPassWithEmptyArray() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", Collections.emptyList(), null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "IS_EMPTY_LIST", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: non-empty array — one element")
    void shouldFailWithOneElement() {
        List<String> list = List.of("1004");
        assertThatThrownBy(() -> operator.apply("$.codes", list, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_EMPTY_LIST failed")
                .hasMessageContaining("1 elements");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "IS_EMPTY_LIST", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: non-empty array — multiple elements")
    void shouldFailWithMultipleElements() {
        List<String> list = Arrays.asList("A", "B", "C");
        assertThatThrownBy(() -> operator.apply("$.codes", list, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_EMPTY_LIST failed")
                .hasMessageContaining("3 elements");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "IS_EMPTY_LIST", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        assertThatThrownBy(() -> operator.apply("$.codes", "scalar", null, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}

