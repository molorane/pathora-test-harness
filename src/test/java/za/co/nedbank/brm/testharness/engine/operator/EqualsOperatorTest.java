package za.co.nedbank.brm.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EqualsOperatorTest {

    private EqualsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new EqualsOperator();
    }

    @Test
    @DisplayName("PASS: string equals string")
    void shouldPassWhenStringsMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "APPROVED", "APPROVED", true));
    }

    @Test
    @DisplayName("PASS: integer equals integer")
    void shouldPassWhenIntegersMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 100, 100, true));
    }

    @Test
    @DisplayName("PASS: double equals double")
    void shouldPassWhenDoublesMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.rate", 3.14, 3.14, true));
    }

    @Test
    @DisplayName("PASS: numeric type coercion — int actual vs double expected")
    void shouldPassWithNumericTypeCoercion() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.count", 5, 5.0, true));
    }

    @Test
    @DisplayName("PASS: string-to-number coercion — number actual vs string expected")
    void shouldPassWithStringToNumberCoercion() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 42, "42", true));
    }

    @Test
    @DisplayName("PASS: null equals null")
    void shouldPassWhenBothNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", null, null, true));
    }

    @Test
    @DisplayName("PASS: boolean equals boolean")
    void shouldPassWhenBooleansMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", true, true, true));
    }

    @Test
    @DisplayName("FAIL: string mismatch")
    void shouldFailWhenStringsMismatch() {
        assertThatThrownBy(() -> operator.apply("$.status", "PENDING", "APPROVED", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: number mismatch")
    void shouldFailWhenNumbersMismatch() {
        assertThatThrownBy(() -> operator.apply("$.score", 50, 100, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: null vs non-null")
    void shouldFailWhenActualIsNullButExpectedIsNot() {
        assertThatThrownBy(() -> operator.apply("$.field", null, "value", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: non-null vs null")
    void shouldFailWhenActualIsNotNullButExpectedIsNull() {
        assertThatThrownBy(() -> operator.apply("$.field", "value", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: different types that cannot coerce")
    void shouldFailWhenTypesCannotCoerce() {
        assertThatThrownBy(() -> operator.apply("$.field", "hello", 42, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("EQUALS failed");
    }
}
