package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContainsAnyOperatorTest {

    private ContainsAnyOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ContainsAnyOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "CONTAINS_ANY", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains one of the expected values")
    void shouldPassWhenOneMatches() {
        Object actual = TestJsonHelper.parse("""
                ["1004", "1020", "1030" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["1004", "1011" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "CONTAINS_ANY", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains all expected values")
    void shouldPassWhenAllMatch() {
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
     * { "JsonPath": "$.items", "Operator": "CONTAINS_ANY", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: last expected value found")
    void shouldPassWhenLastExpectedFound() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B", "C" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["X", "Y", "C" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "CONTAINS_ANY", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: none of the expected values found")
    void shouldFailWhenNoneFound() {
        Object actual = TestJsonHelper.parse("""
                ["1020", "1030" ]
                """);
        Object expected = TestJsonHelper.parse("""
                ["1004", "1011" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ANY failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "CONTAINS_ANY", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: empty actual array")
    void shouldFailWithEmptyActual() {
        Object actual = TestJsonHelper.parse("""
                []
                """);
        Object expected = TestJsonHelper.parse("""
                ["1004", "1011" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("CONTAINS_ANY failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "CONTAINS_ANY", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        Object expected = TestJsonHelper.parse("""
                ["1004", "1011" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", "scalar", expected, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}
