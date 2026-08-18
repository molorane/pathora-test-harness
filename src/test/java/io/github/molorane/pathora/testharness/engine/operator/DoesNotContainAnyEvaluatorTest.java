package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoesNotContainAnyEvaluatorTest {

    private DoesNotContainAnyEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DoesNotContainAnyEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": [2, 9, 5] }
     * ```
     */
    @Test
    @DisplayName("FAIL: array contains at least one of the expected values")
    void shouldFailWhenAnyFound() {
        Object actual = TestJsonHelper.parse("""
                [0, 1, 2]
                """);
        Object expected = TestJsonHelper.parse("""
                [2, 9, 5]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ANY failed")
                .hasMessageContaining("Array contains at least one of");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": ["X", "Y"] }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains none of the expected values")
    void shouldPassWhenNoneFound() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["X", "Y"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": ["A", "B"] }
     * ```
     */
    @Test
    @DisplayName("PASS: empty actual array contains nothing")
    void shouldPassWithEmptyActual() {
        Object actual = TestJsonHelper.parse("""
                []
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": [] }
     * ```
     */
    @Test
    @DisplayName("PASS: empty expected values — nothing to match")
    void shouldPassWithEmptyExpected() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        Object expected = TestJsonHelper.parse("""
                []
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": ["1004", "1011", "1020"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: exact match with one expected value")
    void shouldFailWithExactMatch() {
        Object actual = TestJsonHelper.parse("""
                ["1004", "1011", "1020"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["1004"]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ANY failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": ["A", "B"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: all values match expected")
    void shouldFailWhenAllMatch() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ANY failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": ["B"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: one value matches expected")
    void shouldFailWhenOneMatches() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B", "C"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["B"]
                """);
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ANY failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "DOES_NOT_CONTAIN_ANY", "Value": [100, 200, 300] }
     * ```
     */
    @Test
    @DisplayName("PASS: numeric values with no match")
    void shouldPassWithNumericValuesNoMatch() {
        Object actual = TestJsonHelper.parse("""
                [1, 2, 3]
                """);
        Object expected = TestJsonHelper.parse("""
                [100, 200, 300]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", actual, expected, true));
    }
}

