package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UniqueElementsOperatorTest {

    private UniqueElementsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new UniqueElementsOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "UNIQUE_ELEMENTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: all elements unique")
    void shouldPassWithUniqueElements() {
        List<String> list = Arrays.asList("A", "B", "C");
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", list, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "UNIQUE_ELEMENTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: single element — always unique")
    void shouldPassWithSingleElement() {
        List<String> list = List.of("ONLY");
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", list, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "UNIQUE_ELEMENTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: empty array — vacuously unique")
    void shouldPassWithEmptyArray() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", Collections.emptyList(), null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ids", "Operator": "UNIQUE_ELEMENTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: unique numbers")
    void shouldPassWithUniqueNumbers() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThatNoException().isThrownBy(() -> operator.apply("$.ids", list, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "UNIQUE_ELEMENTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: duplicate string")
    void shouldFailWithDuplicateString() {
        List<String> list = Arrays.asList("A", "B", "A");
        assertThatThrownBy(() -> operator.apply("$.codes", list, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("UNIQUE_ELEMENTS failed")
                .hasMessageContaining("Duplicate found: A");
    }

    /**
     * ```json
     * { "JsonPath": "$.ids", "Operator": "UNIQUE_ELEMENTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: duplicate numbers")
    void shouldFailWithDuplicateNumbers() {
        List<Integer> list = Arrays.asList(1, 2, 2, 3);
        assertThatThrownBy(() -> operator.apply("$.ids", list, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Duplicate found: 2")
                .hasMessageContaining("index 2");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "UNIQUE_ELEMENTS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: all elements same")
    void shouldFailWhenAllSame() {
        List<String> list = Arrays.asList("X", "X", "X");
        assertThatThrownBy(() -> operator.apply("$.codes", list, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("UNIQUE_ELEMENTS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "UNIQUE_ELEMENTS", "Value": null }
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
