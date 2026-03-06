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

class ArraySizeEqualsOperatorTest {

    private ArraySizeEqualsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ArraySizeEqualsOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 3 }
     * ```
     */
    @Test
    @DisplayName("PASS: array size matches expected")
    void shouldPassWhenSizeMatches() {
        List<String> list = Arrays.asList("A", "B", "C");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, 3, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 0 }
     * ```
     */
    @Test
    @DisplayName("PASS: empty array with expected size 0")
    void shouldPassWithEmptyArray() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", Collections.emptyList(), 0, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 0 }
     * ```
     */
    @Test
    @DisplayName("PASS: null actual treated as size 0")
    void shouldPassWhenNullTreatedAsEmpty() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", null, 0, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": "2" }
     * ```
     */
    @Test
    @DisplayName("PASS: expected size as string — coerced to int")
    void shouldPassWithStringExpectedSize() {
        List<String> list = Arrays.asList("A", "B");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, "2", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 1 }
     * ```
     */
    @Test
    @DisplayName("PASS: single element array")
    void shouldPassWithSingleElement() {
        List<String> list = List.of("ONLY");
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, 1, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 2 }
     * ```
     */
    @Test
    @DisplayName("FAIL: array size mismatch — too many elements")
    void shouldFailWhenTooManyElements() {
        List<String> list = Arrays.asList("A", "B", "C");
        assertThatThrownBy(() -> operator.apply("$.items", list, 2, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_SIZE_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 3 }
     * ```
     */
    @Test
    @DisplayName("FAIL: array size mismatch — too few elements")
    void shouldFailWhenTooFewElements() {
        List<String> list = List.of("A");
        assertThatThrownBy(() -> operator.apply("$.items", list, 3, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_SIZE_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 1 }
     * ```
     */
    @Test
    @DisplayName("FAIL: non-list value")
    void shouldFailWhenNotAList() {
        assertThatThrownBy(() -> operator.apply("$.items", "not-a-list", 1, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Expected array at path");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_SIZE_EQUALS", "Value": 1 }
     * ```
     */
    @Test
    @DisplayName("FAIL: null actual with expected size > 0")
    void shouldFailWhenNullWithNonZeroExpected() {
        assertThatThrownBy(() -> operator.apply("$.items", null, 1, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_SIZE_EQUALS failed");
    }
}
