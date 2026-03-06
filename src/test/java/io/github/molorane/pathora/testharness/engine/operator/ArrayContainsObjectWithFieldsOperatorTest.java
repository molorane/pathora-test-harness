package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayContainsObjectWithFieldsOperatorTest {

    private ArrayContainsObjectWithFieldsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new ArrayContainsObjectWithFieldsOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: array contains object with matching field")
    void shouldPassWhenObjectMatches() {
        Object list = TestJsonHelper.parse("""
                [
                  {
                    "type": "1035",
                    "status": "ACTIVE"
                  }
                ]
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "type": "1035"
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: multiple objects — one matches")
    void shouldPassWhenOneOfManyMatches() {
        Object list = TestJsonHelper.parse("""
                [
                  {
                    "type": "1040"
                  },
                  {
                    "type": "1035",
                    "status": "ACTIVE"
                  }
                ]
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "type": "1035"
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: matching multiple fields")
    void shouldPassWhenMultipleFieldsMatch() {
        Object list = TestJsonHelper.parse("""
                [
                  {
                    "type": "1035",
                    "status": "ACTIVE",
                    "code": "X"
                  }
                ]
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "type": "1035",
                  "status": "ACTIVE"
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.items", list, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: no object matches")
    void shouldFailWhenNoObjectMatches() {
        Object list = TestJsonHelper.parse("""
                [
                  {
                    "type": "1040"
                  }
                ]
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "type": "1035"
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.items", list, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_OBJECT_WITH_FIELDS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: empty array")
    void shouldFailWithEmptyArray() {
        Object list = TestJsonHelper.parse("""
                []
                """);
        Object expected = TestJsonHelper.parse("""
                {
                  "type": "1035"
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.items", list, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_OBJECT_WITH_FIELDS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual is not a list")
    void shouldFailWhenActualIsNotList() {
        Object expected = TestJsonHelper.parse("""
                {
                  "type": "1035"
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.items", "not-a-list", expected, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected array at path");
    }

    /**
     * ```json
     * { "JsonPath": "$.items", "Operator": "ARRAY_CONTAINS_OBJECT_WITH_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: object has field but wrong value")
    void shouldFailWhenFieldValueMismatch() {
        Object list = TestJsonHelper.parse("""
                [
                  {
                    "type": "1040",
                    "status": "ACTIVE"
                  }
                ]
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "type": "1035"
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.items", list, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ARRAY_CONTAINS_OBJECT_WITH_FIELDS failed");
    }
}
