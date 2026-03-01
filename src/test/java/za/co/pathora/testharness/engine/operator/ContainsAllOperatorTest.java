package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContainsAllOperatorTest {

    private ContainsAllOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ContainsAllOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "CONTAINS_ALL", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains all expected values")
    void shouldPassWhenAllFound() {
        Object actual = TestJsonHelper.parse("""
                ["1004", "1011", "1020" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["1004", "1011" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "CONTAINS_ALL", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: exact match")
    void shouldPassWithExactMatch() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "CONTAINS_ALL", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: different order")
    void shouldPassWithDifferentOrder() {
        Object actual = TestJsonHelper.parse("""
                ["B", "A" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "CONTAINS_ALL", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: one expected value missing")
    void shouldFailWhenOneMissing() {
        Object actual = TestJsonHelper.parse("""
                ["1004", "1020" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["1004", "1011" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ALL failed")
                .hasMessageContaining("Missing value: 1011");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "CONTAINS_ALL", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: none of the expected values found")
    void shouldFailWhenNoneFound() {
        Object actual = TestJsonHelper.parse("""
                ["X", "Y" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ALL failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "CONTAINS_ALL", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: empty actual array")
    void shouldFailWithEmptyActual() {
        Object actual = TestJsonHelper.parse("""
                []
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ALL failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "CONTAINS_ALL", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: empty expected — vacuously true")
    void shouldPassWithEmptyExpected() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B" ]
                """);
        Object expected = TestJsonHelper.parse("""
                []
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }
}
