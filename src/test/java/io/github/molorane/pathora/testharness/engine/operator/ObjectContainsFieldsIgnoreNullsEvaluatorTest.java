package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectContainsFieldsIgnoreNullsEvaluatorTest {

    private ObjectContainsFieldsIgnoreNullsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ObjectContainsFieldsIgnoreNullsEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: null expected fields are ignored")
    void shouldPassIgnoringNullExpectedFields() {
        Object actual = TestJsonHelper.parse("""
                {
                  "clientType": "1031"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "middleName": null
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: all non-null fields match")
    void shouldPassWhenNonNullFieldsMatch() {
        Object actual = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "riskLevel": "HIGH",
                  "segment": "Retail"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "riskLevel": "HIGH",
                  "optionalField": null
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.data", "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: all expected fields are null — always passes")
    void shouldPassWhenAllExpectedFieldsAreNull() {
        Object actual = TestJsonHelper.parse("""
                {
                  "anything": "value"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "field1": null,
                  "field2": null
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: non-null expected field value mismatch")
    void shouldFailWhenNonNullFieldMismatch() {
        Object actual = TestJsonHelper.parse("""
                {
                  "clientType": "1032"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "middleName": null
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS_IGNORE_NULLS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: non-null expected field missing in actual")
    void shouldFailWhenNonNullFieldMissing() {
        Object actual = TestJsonHelper.parse("""
                {
                  "otherField": "value"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "middleName": null
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS_IGNORE_NULLS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.data", "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: exact match with no null fields")
    void shouldPassWithExactMatchNoNulls() {
        Object actual = TestJsonHelper.parse("""
                {
                  "a": "1",
                  "b": "2"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "a": "1",
                  "b": "2"
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.data", "Operator": "OBJECT_CONTAINS_FIELDS_IGNORE_NULLS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual is not a map")
    void shouldFailWhenActualIsNotMap() {
        Object expected = TestJsonHelper.parse("""
                {
                  "field": "value"
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.data", "not-a-map", expected, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected object but got");
    }
}
