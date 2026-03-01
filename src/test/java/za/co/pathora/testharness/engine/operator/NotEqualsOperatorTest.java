package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotEqualsOperatorTest {

    private NotEqualsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new NotEqualsOperator();
    }

    @Test
    @DisplayName("PASS: different strings")
    void shouldPassWhenStringsDiffer() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "APPROVED", "DECLINED", true));
    }

    @Test
    @DisplayName("PASS: different numbers")
    void shouldPassWhenNumbersDiffer() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 50, 100, true));
    }

    @Test
    @DisplayName("PASS: null actual vs non-null expected")
    void shouldPassWhenActualIsNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", null, "value", true));
    }

    @Test
    @DisplayName("PASS: non-null actual vs null expected")
    void shouldPassWhenExpectedIsNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "value", null, true));
    }

    @Test
    @DisplayName("PASS: different types that cannot coerce")
    void shouldPassWhenTypesCannotCoerce() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", "hello", 42, true));
    }

    @Test
    @DisplayName("FAIL: same strings")
    void shouldFailWhenStringsMatch() {
        assertThatThrownBy(() -> operator.apply("$.status", "DECLINED", "DECLINED", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: same numbers")
    void shouldFailWhenNumbersMatch() {
        assertThatThrownBy(() -> operator.apply("$.score", 100, 100, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: numeric coercion makes them equal")
    void shouldFailWithNumericCoercion() {
        assertThatThrownBy(() -> operator.apply("$.score", 5, 5.0, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: string-to-number coercion makes them equal")
    void shouldFailWithStringToNumberCoercion() {
        assertThatThrownBy(() -> operator.apply("$.score", 42, "42", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: both null")
    void shouldFailWhenBothNull() {
        assertThatThrownBy(() -> operator.apply("$.field", null, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NOT_EQUALS failed");
    }
}
