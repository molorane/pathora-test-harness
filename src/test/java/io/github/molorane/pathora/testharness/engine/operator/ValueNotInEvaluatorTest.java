package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueNotInEvaluatorTest {

    private ValueNotInEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ValueNotInEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_NOT_IN", "Value": ["APPROVED", "PENDING"] }
     * ```
     */
    @Test
    @DisplayName("PASS: scalar value is not in the expected list")
    void shouldPassWhenValueNotInList() {
        Object expected = TestJsonHelper.parse("""
                ["APPROVED", "PENDING"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "DECLINED", expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "VALUE_NOT_IN", "Value": [1004, 1011, 1020] }
     * ```
     */
    @Test
    @DisplayName("PASS: numeric scalar value is not in the expected list")
    void shouldPassWhenNumericValueNotInList() {
        Object expected = TestJsonHelper.parse("""
                [1004, 1011, 1020]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", 1099, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "VALUE_NOT_IN", "Value": ["A", "B"] }
     * ```
     */
    @Test
    @DisplayName("PASS: value not in list — different values")
    void shouldPassWhenValueNotInListDifferentValues() {
        Object expected = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", "C", expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_NOT_IN", "Value": [] }
     * ```
     */
    @Test
    @DisplayName("PASS: empty expected list (value is vacuously not in empty list)")
    void shouldPassWithEmptyExpectedList() {
        Object expected = TestJsonHelper.parse("""
                []
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "APPROVED", expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_NOT_IN", "Value": ["APPROVED", "PENDING"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: scalar value is in the expected list")
    void shouldFailWhenValueInList() {
        Object expected = TestJsonHelper.parse("""
                ["APPROVED", "PENDING"]
                """);
        assertThatThrownBy(() -> operator.apply("$.status", "APPROVED", expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("VALUE_NOT_IN failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "VALUE_NOT_IN", "Value": [100, 200, 300] }
     * ```
     */
    @Test
    @DisplayName("FAIL: numeric value is in the expected list")
    void shouldFailWhenNumericValueInList() {
        Object expected = TestJsonHelper.parse("""
                [100, 200, 300]
                """);
        assertThatThrownBy(() -> operator.apply("$.code", 200, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("VALUE_NOT_IN failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "VALUE_NOT_IN", "Value": "not-a-list" }
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

