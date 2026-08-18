package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueInEvaluatorTest {

    private ValueInEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ValueInEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_IN", "Value": ["APPROVED", "PENDING"] }
     * ```
     */
    @Test
    @DisplayName("PASS: scalar value is in the expected list")
    void shouldPassWhenValueInList() {
        Object expected = TestJsonHelper.parse("""
                ["APPROVED", "PENDING"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "APPROVED", expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "VALUE_IN", "Value": [1004, 1011, 1020] }
     * ```
     */
    @Test
    @DisplayName("PASS: numeric scalar value is in the expected list")
    void shouldPassWhenNumericValueInList() {
        Object expected = TestJsonHelper.parse("""
                [1004, 1011, 1020]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", 1011, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "VALUE_IN", "Value": ["A", "B", "C"] }
     * ```
     */
    @Test
    @DisplayName("PASS: value is first element in list")
    void shouldPassWhenValueIsFirstElement() {
        Object expected = TestJsonHelper.parse("""
                ["A", "B", "C"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", "A", expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "VALUE_IN", "Value": ["A", "B", "C"] }
     * ```
     */
    @Test
    @DisplayName("PASS: value is last element in list")
    void shouldPassWhenValueIsLastElement() {
        Object expected = TestJsonHelper.parse("""
                ["A", "B", "C"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", "C", expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_IN", "Value": ["APPROVED", "PENDING"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: scalar value is not in the expected list")
    void shouldFailWhenValueNotInList() {
        Object expected = TestJsonHelper.parse("""
                ["APPROVED", "PENDING"]
                """);
        assertThatThrownBy(() -> operator.apply("$.status", "DECLINED", expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("VALUE_IN failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_IN", "Value": [] }
     * ```
     */
    @Test
    @DisplayName("FAIL: empty expected list")
    void shouldFailWithEmptyExpectedList() {
        Object expected = TestJsonHelper.parse("""
                []
                """);
        assertThatThrownBy(() -> operator.apply("$.status", "APPROVED", expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("VALUE_IN failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_IN", "Value": "not-a-list" }
     * ```
     */
    @Test
    @DisplayName("FAIL: expected is not a list")
    void shouldFailWhenExpectedIsNotList() {
        assertThatThrownBy(() -> operator.apply("$.status", "APPROVED", "not-a-list", true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }
}

