package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoesNotContainAllOperatorTest {

    private DoesNotContainAllOperator operator;

    @BeforeEach
    void setUp() {
        operator = new DoesNotContainAllOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "DOES_NOT_CONTAIN_ALL", "Value": [8, 0, 3] }
     * ```
     */
    @Test
    @DisplayName("PASS: array does not contain all expected values (missing some)")
    void shouldPassWhenNotAllFound() {
        Object actual = TestJsonHelper.parse("""
                [0, 1, 2]
                """);
        Object expected = TestJsonHelper.parse("""
                [8, 0, 3]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.codes", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ALL", "Value": ["A", "B"] }
     * ```
     */
    @Test
    @DisplayName("PASS: missing one value from expected list")
    void shouldPassWhenOneMissing() {
        Object actual = TestJsonHelper.parse("""
                ["A", "C"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.items", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ALL", "Value": ["A"] }
     * ```
     */
    @Test
    @DisplayName("PASS: empty actual array cannot contain all values")
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
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ALL", "Value": {} }
     * ```
     */
    @Test
    @DisplayName("FAIL: empty expected — array contains all zero items (vacuously true)")
    void shouldFailWithEmptyExpected() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        Object expected = TestJsonHelper.parse("""
                []
                """);
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ALL failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.codes", "Operator": "DOES_NOT_CONTAIN_ALL", "Value": ["1004", "1011"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: array contains all expected values")
    void shouldFailWhenAllFound() {
        Object actual = TestJsonHelper.parse("""
                ["1004", "1011", "1020"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["1004", "1011"]
                """);
        assertThatThrownBy(() -> operator.apply("$.codes", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ALL failed")
                .hasMessageContaining("Array contains all of");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ALL", "Value": ["A", "B"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: exact match")
    void shouldFailWithExactMatch() {
        Object actual = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ALL failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "DOES_NOT_CONTAIN_ALL", "Value": ["A", "B"] }
     * ```
     */
    @Test
    @DisplayName("FAIL: different order but all values present")
    void shouldFailWithDifferentOrderButAllPresent() {
        Object actual = TestJsonHelper.parse("""
                ["B", "A"]
                """);
        Object expected = TestJsonHelper.parse("""
                ["A", "B"]
                """);
        assertThatThrownBy(() -> operator.apply("$.items", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("DOES_NOT_CONTAIN_ALL failed");
    }
}

