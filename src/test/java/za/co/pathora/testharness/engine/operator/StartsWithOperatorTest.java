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

    @Test
    @DisplayName("PASS: string starts with prefix")
    void shouldPassWhenStartsWithPrefix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.refId", "REF-1234-56789", "REF-", true));
    }

    @Test
    @DisplayName("PASS: exact match counts as starts with")
    void shouldPassWhenExactMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "ABC", "ABC", true));
    }

    @Test
    @DisplayName("PASS: single character prefix")
    void shouldPassWithSingleCharPrefix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "John", "J", true));
    }

    @Test
    @DisplayName("PASS: empty prefix — always matches")
    void shouldPassWithEmptyPrefix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "anything", "", true));
    }

    @Test
    @DisplayName("PASS: numeric actual converted to string")
    void shouldPassWithNumericActual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", 12345, "123", true));
    }

    @Test
    @DisplayName("FAIL: string does not start with prefix")
    void shouldFailWhenDoesNotStartWith() {
        assertThatThrownBy(() -> operator.apply("$.refId", "INV-1234", "REF-", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }

    @Test
    @DisplayName("FAIL: case mismatch")
    void shouldFailOnCaseMismatch() {
        assertThatThrownBy(() -> operator.apply("$.refId", "ref-1234", "REF-", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }

    @Test
    @DisplayName("FAIL: prefix longer than actual")
    void shouldFailWhenPrefixLongerThanActual() {
        assertThatThrownBy(() -> operator.apply("$.code", "AB", "ABCDEF", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }

    @Test
    @DisplayName("FAIL: empty actual with non-empty prefix")
    void shouldFailWhenActualIsEmpty() {
        assertThatThrownBy(() -> operator.apply("$.field", "", "REF-", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STARTS_WITH failed");
    }
}
