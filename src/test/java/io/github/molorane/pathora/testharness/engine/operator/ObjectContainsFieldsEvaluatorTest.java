package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectContainsFieldsEvaluatorTest {

    private ObjectContainsFieldsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new ObjectContainsFieldsEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: actual contains all expected fields")
    void shouldPassWhenAllFieldsMatch() {
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
                  "riskLevel": "HIGH"
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.result", "Operator": "OBJECT_CONTAINS_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: exact match — no extra fields")
    void shouldPassWithExactMatch() {
        Object actual = TestJsonHelper.parse("""
                {
                  "status": "APPROVED"
                }
                """);
        Object expected = TestJsonHelper.parse("""
                {
                  "status": "APPROVED"
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.result", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.data", "Operator": "OBJECT_CONTAINS_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: nested map matches")
    void shouldPassWithNestedMap() {
        Object actual = TestJsonHelper.parse("""
                {
                  "nested": {
                    "code": "X"
                  }
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "nested": {
                    "code": "X"
                  }
                }
                """);

        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: field value mismatch")
    void shouldFailWhenFieldValueDiffers() {
        Object actual = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "riskLevel": "LOW"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "riskLevel": "HIGH"
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: expected field missing in actual")
    void shouldFailWhenFieldMissing() {
        Object actual = TestJsonHelper.parse("""
                {
                  "clientType": "1031"
                }
                """);

        Object expected = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "riskLevel": "HIGH"
                }
                """);

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: null expected field does NOT match missing")
    void shouldFailWhenExpectedNullButFieldMissing() {
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

        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("OBJECT_CONTAINS_FIELDS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "OBJECT_CONTAINS_FIELDS", "Value": {...} }
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

        assertThatThrownBy(() -> operator.apply("$.client", "not-a-map", expected, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected object but got");
    }
}
