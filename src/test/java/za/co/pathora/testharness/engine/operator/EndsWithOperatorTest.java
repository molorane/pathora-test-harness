package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EndsWithOperatorTest {

    private EndsWithOperator operator;

    @BeforeEach
    void setUp() {
        operator = new EndsWithOperator();
    }

    @Test
    @DisplayName("PASS: string ends with suffix")
    void shouldPassWhenEndsWith() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.refId", "REF-1234-123", "123", true));
    }

    @Test
    @DisplayName("PASS: exact match counts as ends with")
    void shouldPassWhenExactMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "ABC", "ABC", true));
    }

    @Test
    @DisplayName("PASS: single character suffix")
    void shouldPassWithSingleCharSuffix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "John", "n", true));
    }

    @Test
    @DisplayName("PASS: empty suffix — always matches")
    void shouldPassWithEmptySuffix() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "anything", "", true));
    }

    @Test
    @DisplayName("PASS: numeric actual converted to string")
    void shouldPassWithNumericActual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", 12345, "345", true));
    }

    @Test
    @DisplayName("FAIL: string does not end with suffix")
    void shouldFailWhenDoesNotEndWith() {
        assertThatThrownBy(() -> operator.apply("$.refId", "REF-1234", "5678", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ENDS_WITH failed");
    }

    @Test
    @DisplayName("FAIL: case mismatch")
    void shouldFailOnCaseMismatch() {
        assertThatThrownBy(() -> operator.apply("$.file", "report.PDF", ".pdf", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ENDS_WITH failed");
    }

    @Test
    @DisplayName("FAIL: suffix longer than actual")
    void shouldFailWhenSuffixLongerThanActual() {
        assertThatThrownBy(() -> operator.apply("$.code", "AB", "ABCDEF", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ENDS_WITH failed");
    }
}
