package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EqualsOperatorTest {

    private EqualsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new EqualsOperator();
    }

    /**
     * ```json
     * {
     * "JsonPath":
     * "$.status",
     * "Operator": "EQUALS",
     * "Value": "APPROVED"
     * }
     * ```
     */
    @Test
    @DisplayName("PASS: string equals string")
    void shouldPassWhenStringsMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "APPROVED", "APPROVED", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "EQUALS", "Value": 100 }
     * ```
     */
    @Test
    @DisplayName("PASS: integer equals integer")
    void shouldPassWhenIntegersMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 100, 100, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.rate", "Operator": "EQUALS", "Value": 3.14 }
     * ```
     */
    @Test
    @DisplayName("PASS: double equals double")
    void shouldPassWhenDoublesMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.rate", 3.14, 3.14, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.count", "Operator": "EQUALS", "Value": 5.0 }
     * ```
     */
    @Test
    @DisplayName("PASS: numeric type coercion — int actual vs double expected")
    void shouldPassWithNumericTypeCoercion() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.count", 5, 5.0, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "EQUALS", "Value": "42" }
     * ```
     */
    @Test
    @DisplayName("PASS: string-to-number coercion — number actual vs string expected")
    void shouldPassWithStringToNumberCoercion() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 42, "42", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "EQUALS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: null equals null")
    void shouldPassWhenBothNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", null, null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.active", "Operator": "EQUALS", "Value": true }
     * ```
     */
    @Test
    @DisplayName("PASS: boolean equals boolean")
    void shouldPassWhenBooleansMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", true, true, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.active", "Operator": "EQUALS", "Value": true }
     * ```
     */
    @Test
    @DisplayName("PASS: boolean equals boolean")
    void shouldFailWhenExpectedIsBooleanAndActualIsString() {
        assertThatThrownBy(() -> operator.apply("$.active", "true", true, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.active", "Operator": "EQUALS", "Value": true }
     * ```
     */
    @Test
    @DisplayName("PASS: boolean equals boolean")
    void shouldFailWhenActualIsBooleanAndExpectedIsString() {
        assertThatThrownBy(() -> operator.apply("$.active", true, "true", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "EQUALS", "Value": "APPROVED" }
     * ```
     */
    @Test
    @DisplayName("FAIL: string mismatch")
    void shouldFailWhenStringsMismatch() {
        assertThatThrownBy(() -> operator.apply("$.status", "PENDING", "APPROVED", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "EQUALS", "Value": 100 }
     * ```
     */
    @Test
    @DisplayName("FAIL: number mismatch")
    void shouldFailWhenNumbersMismatch() {
        assertThatThrownBy(() -> operator.apply("$.score", 50, 100, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "EQUALS", "Value": "value" }
     * ```
     */
    @Test
    @DisplayName("FAIL: null vs non-null")
    void shouldFailWhenActualIsNullButExpectedIsNot() {
        assertThatThrownBy(() -> operator.apply("$.field", null, "value", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "EQUALS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: non-null vs null")
    void shouldFailWhenActualIsNotNullButExpectedIsNull() {
        assertThatThrownBy(() -> operator.apply("$.field", "value", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "EQUALS", "Value": 42 }
     * ```
     */
    @Test
    @DisplayName("FAIL: different types that cannot coerce")
    void shouldFailWhenTypesCannotCoerce() {
        assertThatThrownBy(() -> operator.apply("$.field", "hello", 42, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }
}
