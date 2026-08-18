package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LessThanOrEqualsEvaluatorTest {

    private LessThanOrEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new LessThanOrEqualsEvaluator();
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "LESS_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("PASS: actual less than expected")
    void shouldPassWhenLess() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 30, 50, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "LESS_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("PASS: actual equals expected")
    void shouldPassWhenEqual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 50, 50, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.rate", "Operator": "LESS_THAN_OR_EQUALS", "Value": 5 }
     * ```
     */
    @Test
    @DisplayName("PASS: double actual <= int expected")
    void shouldPassWithMixedTypes() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.rate", 5.0, 5, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.temp", "Operator": "LESS_THAN_OR_EQUALS", "Value": -5 }
     * ```
     */
    @Test
    @DisplayName("PASS: negative numbers — actual <= expected")
    void shouldPassWithNegatives() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.temp", -10, -5, true));
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "LESS_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("FAIL: actual greater than expected")
    void shouldFailWhenGreater() {
        assertThatThrownBy(() -> operator.apply("$.score", 60, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LESS_THAN_OR_EQUALS failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.score", "Operator": "LESS_THAN_OR_EQUALS", "Value": 50 }
     * ```
     */
    @Test
    @DisplayName("FAIL: just above expected")
    void shouldFailWhenJustAbove() {
        assertThatThrownBy(() -> operator.apply("$.score", 50.01, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LESS_THAN_OR_EQUALS failed");
    }
}
