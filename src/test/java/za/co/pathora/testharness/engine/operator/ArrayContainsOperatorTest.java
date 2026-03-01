package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayContainsOperatorTest {

    private ArrayContainsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ArrayContainsOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "ARRAY_CONTAINS", "Value": "1004" }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains the expected string value")
    void shouldPassWhenArrayContainsString() {
        Object list = TestJsonHelper.parse("""
                ["1004", "1011" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", list, "1004", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.ids", "Operator": "ARRAY_CONTAINS", "Value": 2.0 }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains the expected numeric value")
    void shouldPassWhenArrayContainsNumber() {
        Object list = TestJsonHelper.parse("""
                [1.0, 2.0, 3.0 ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.ids", list, 2.0, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS", "Value": "ONLY" }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains value — single element")
    void shouldPassWithSingleElementArray() {
        Object list = TestJsonHelper.parse("""
                ["ONLY" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, "ONLY", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS", "Value": "C" }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains value — last element")
    void shouldPassWhenValueIsLastElement() {
        Object list = TestJsonHelper.parse("""
                ["A", "B", "C" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, "C", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "ARRAY_CONTAINS", "Value": "1004" }
     * ```
     */
    @Test
    @DisplayName("FAIL: array does not contain expected value")
    void shouldFailWhenValueNotInArray() {
        Object list = TestJsonHelper.parse("""
                ["1011", "1012" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", list, "1004", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "ARRAY_CONTAINS", "Value": "1004" }
     * ```
     */
    @Test
    @DisplayName("FAIL: empty array")
    void shouldFailWhenArrayIsEmpty() {
        Object list = TestJsonHelper.parse("""
                []
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", list, "1004", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "ARRAY_CONTAINS", "Value": "1004" }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        assertThatThrownBy(() -> operator.apply("$.codes", "not-a-list", "1004", true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}
