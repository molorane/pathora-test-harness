package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EndsWithOperatorTest {

    private EndsWithOperator operator;

    @BeforeEach
    void setUp() {
        operator = new EndsWithOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.refId", "Operator": "ENDS_WITH", "Value": "123" }
     * ```
     */
    @Test
    @DisplayName("PASS: string ends with suffix")
    void shouldPassWhenEndsWith() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.refId", "REF-1234-123", "123", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "ENDS_WITH", "Value": "ABC" }
     * ```
     */
    @Test
    @DisplayName("PASS: exact match counts as ends with")
    void shouldPassWhenExactMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "ABC", "ABC", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.name", "Operator": "ENDS_WITH", "Value": "n" }
     * ```
     */
    @Test
    @DisplayName("PASS: single character suffix")
    void shouldPassWithSingleCharSuffix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "John", "n", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "ENDS_WITH", "Value": "" }
     * ```
     */
    @Test
    @DisplayName("PASS: empty suffix — always matches")
    void shouldPassWithEmptySuffix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "anything", "", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "ENDS_WITH", "Value": "345" }
     * ```
     */
    @Test
    @DisplayName("PASS: numeric actual converted to string")
    void shouldPassWithNumericActual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", 12345, "345", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.refId", "Operator": "ENDS_WITH", "Value": "5678" }
     * ```
     */
    @Test
    @DisplayName("FAIL: string does not end with suffix")
    void shouldFailWhenDoesNotEndWith() {
        assertThatThrownBy(() -> operator.apply("$.refId", "REF-1234", "5678", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ENDS_WITH failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.file", "Operator": "ENDS_WITH", "Value": ".pdf" }
     * ```
     */
    @Test
    @DisplayName("FAIL: case mismatch")
    void shouldFailOnCaseMismatch() {
        assertThatThrownBy(() -> operator.apply("$.file", "report.PDF", ".pdf", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ENDS_WITH failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "ENDS_WITH", "Value": "ABCDEF" }
     * ```
     */
    @Test
    @DisplayName("FAIL: suffix longer than actual")
    void shouldFailWhenSuffixLongerThanActual() {
        assertThatThrownBy(() -> operator.apply("$.code", "AB", "ABCDEF", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ENDS_WITH failed");
    }
}
