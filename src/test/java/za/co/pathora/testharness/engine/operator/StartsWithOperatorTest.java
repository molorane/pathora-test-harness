package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StartsWithOperatorTest {

    private StartsWithOperator operator;

    @BeforeEach
    void setUp() {
        operator = new StartsWithOperator();
    }

    /**
     * ```json
     * { "JsonPath": "$.refId", "Operator": "STARTS_WITH", "Value": "REF-" }
     * ```
     */
    @Test
    @DisplayName("PASS: string starts with prefix")
    void shouldPassWhenStartsWithPrefix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.refId", "REF-1234-56789", "REF-", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "STARTS_WITH", "Value": "ABC" }
     * ```
     */
    @Test
    @DisplayName("PASS: exact match counts as starts with")
    void shouldPassWhenExactMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "ABC", "ABC", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.name", "Operator": "STARTS_WITH", "Value": "J" }
     * ```
     */
    @Test
    @DisplayName("PASS: single character prefix")
    void shouldPassWithSingleCharPrefix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "John", "J", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "STARTS_WITH", "Value": "" }
     * ```
     */
    @Test
    @DisplayName("PASS: empty prefix — always matches")
    void shouldPassWithEmptyPrefix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "anything", "", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "STARTS_WITH", "Value": "123" }
     * ```
     */
    @Test
    @DisplayName("PASS: numeric actual converted to string")
    void shouldPassWithNumericActual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", 12345, "123", true));
    }

    /**
     * ```json
     * { "JsonPath": "$.refId", "Operator": "STARTS_WITH", "Value": "REF-" }
     * ```
     */
    @Test
    @DisplayName("FAIL: string does not start with prefix")
    void shouldFailWhenDoesNotStartWith() {
        assertThatThrownBy(() -> operator.apply("$.refId", "INV-1234", "REF-", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.refId", "Operator": "STARTS_WITH", "Value": "REF-" }
     * ```
     */
    @Test
    @DisplayName("FAIL: case mismatch")
    void shouldFailOnCaseMismatch() {
        assertThatThrownBy(() -> operator.apply("$.refId", "ref-1234", "REF-", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.code", "Operator": "STARTS_WITH", "Value": "ABCDEF" }
     * ```
     */
    @Test
    @DisplayName("FAIL: prefix longer than actual")
    void shouldFailWhenPrefixLongerThanActual() {
        assertThatThrownBy(() -> operator.apply("$.code", "AB", "ABCDEF", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }

    /**
     * ```json
     * { "JsonPath": "$.field", "Operator": "STARTS_WITH", "Value": "REF-" }
     * ```
     */
    @Test
    @DisplayName("FAIL: empty actual with non-empty prefix")
    void shouldFailWhenActualIsEmpty() {
        assertThatThrownBy(() -> operator.apply("$.field", "", "REF-", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }
}
