package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotEqualsEvaluatorTest {

    private NotEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new NotEqualsEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "NOT_EQUALS", "Value": "DECLINED" }
     * ```
     */
    @Test
    @DisplayName("PASS: different strings")
    void shouldPassWhenStringsDiffer() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "APPROVED", "DECLINED", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "NOT_EQUALS", "Value": 100 }
     * ```
     */
    @Test
    @DisplayName("PASS: different numbers")
    void shouldPassWhenNumbersDiffer() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 50, 100, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "NOT_EQUALS", "Value": "value" }
     * ```
     */
    @Test
    @DisplayName("PASS: null actual vs non-null expected")
    void shouldPassWhenActualIsNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", null, "value", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "NOT_EQUALS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("PASS: non-null actual vs null expected")
    void shouldPassWhenExpectedIsNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "value", null, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "NOT_EQUALS", "Value": 42 }
     * ```
     */
    @Test
    @DisplayName("PASS: different types that cannot coerce")
    void shouldPassWhenTypesCannotCoerce() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "hello", 42, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.status", "Operator": "NOT_EQUALS", "Value": "DECLINED" }
     * ```
     */
    @Test
    @DisplayName("FAIL: same strings")
    void shouldFailWhenStringsMatch() {
        assertThatThrownBy(() -> operator.apply("$.status", "DECLINED", "DECLINED", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "NOT_EQUALS", "Value": 100 }
     * ```
     */
    @Test
    @DisplayName("FAIL: same numbers")
    void shouldFailWhenNumbersMatch() {
        assertThatThrownBy(() -> operator.apply("$.score", 100, 100, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "NOT_EQUALS", "Value": 5.0 }
     * ```
     */
    @Test
    @DisplayName("FAIL: numeric coercion makes them equal")
    void shouldFailWithNumericCoercion() {
        assertThatThrownBy(() -> operator.apply("$.score", 5, 5.0, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "NOT_EQUALS", "Value": "42" }
     * ```
     */
    @Test
    @DisplayName("FAIL: string-to-number coercion makes them equal")
    void shouldFailWithStringToNumberCoercion() {
        assertThatThrownBy(() -> operator.apply("$.score", 42, "42", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "NOT_EQUALS", "Value": null }
     * ```
     */
    @Test
    @DisplayName("FAIL: both null")
    void shouldFailWhenBothNull() {
        assertThatThrownBy(() -> operator.apply("$.field", null, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }
}
