package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HasKeysOperatorTest {

    private HasKeysOperator operator;

    @BeforeEach
    void setUp() {
        operator = new HasKeysOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "HAS_KEYS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: object has all expected keys")
    void shouldPassWhenAllKeysPresent() {
        Object actual = TestJsonHelper.parse("""
                {
                  "clientType": "1031",
                  "riskLevel": "HIGH",
                  "segment": "Retail"
                }
                """);
        Object expected = TestJsonHelper.parse("""
                ["clientType", "riskLevel" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.client", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.data", "Operator": "HAS_KEYS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: exact keys — no extras")
    void shouldPassWithExactKeys() {
        Object actual = TestJsonHelper.parse("""
                {
                  "a": 1,
                  "b": 2
                }
                """);
        Object expected = TestJsonHelper.parse("""
                ["a", "b" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.data", "Operator": "HAS_KEYS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("PASS: single key")
    void shouldPassWithSingleKey() {
        Object actual = TestJsonHelper.parse("""
                {
                  "status": "ACTIVE"
                }
                """);
        Object expected = TestJsonHelper.parse("""
                ["status" ]
                """);
        assertThatNoException().isThrownBy(() -> operator.apply("$.data", actual, expected, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "HAS_KEYS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: one key missing")
    void shouldFailWhenOneKeyMissing() {
        Object actual = TestJsonHelper.parse("""
                {
                  "clientType": "1031"
                }
                """);
        Object expected = TestJsonHelper.parse("""
                ["clientType", "riskLevel" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("HAS_KEYS failed")
                .hasMessageContaining("riskLevel");
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "HAS_KEYS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: all keys missing")
    void shouldFailWhenAllKeysMissing() {
        Object actual = TestJsonHelper.parse("""
                {
                  "other": "value"
                }
                """);
        Object expected = TestJsonHelper.parse("""
                ["clientType", "riskLevel" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.client", actual, expected, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("HAS_KEYS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.client", "Operator": "HAS_KEYS", "Value": {...} }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual is not a map")
    void shouldFailWhenActualIsNotMap() {
        Object expected = TestJsonHelper.parse("""
                ["clientType", "riskLevel" ]
                """);
        assertThatThrownBy(() -> operator.apply("$.client", "not-a-map", expected, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected object but got");
    }
}
