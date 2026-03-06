package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GreaterThanOrEqualsOperatorTest {

    private GreaterThanOrEqualsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new GreaterThanOrEqualsOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "GREATER_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("PASS: actual greater than expected")
    void shouldPassWhenGreater() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 75, 50, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "GREATER_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("PASS: actual equals expected")
    void shouldPassWhenEqual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 50, 50, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.rate", "Operator": "GREATER_THAN_OR_EQUALS", "Value": 5 }
     * ```
     */
    @Test
    @DisplayName("PASS: double actual >= int expected")
    void shouldPassWithMixedTypes() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.rate", 5.0, 5, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "GREATER_THAN_OR_EQUALS", "Value": "50" }
     * ```
     */
    @Test
    @DisplayName("PASS: string-to-number coercion")
    void shouldPassWithStringCoercion() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 100, "50", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.temp", "Operator": "GREATER_THAN_OR_EQUALS", "Value": -10 }
     * ```
     */
    @Test
    @DisplayName("PASS: negative numbers — actual >= expected")
    void shouldPassWithNegatives() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.temp", -5, -10, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "GREATER_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual less than expected")
    void shouldFailWhenLess() {
        assertThatThrownBy(() -> operator.apply("$.score", 45, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("GREATER_THAN_OR_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "GREATER_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("FAIL: just below expected")
    void shouldFailWhenJustBelow() {
        assertThatThrownBy(() -> operator.apply("$.score", 49.99, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("GREATER_THAN_OR_EQUALS failed");
    }
}
