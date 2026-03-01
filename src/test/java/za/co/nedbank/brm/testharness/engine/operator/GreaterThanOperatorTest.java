package za.co.nedbank.brm.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.nedbank.brm.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GreaterThanOperatorTest {

    private GreaterThanOperator operator;

    @BeforeEach
    void setUp() {
        operator = new GreaterThanOperator();
    }

    @Test
    @DisplayName("PASS: actual greater than expected")
    void shouldPassWhenActualIsGreater() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 75, 50, true));
    }

    @Test
    @DisplayName("PASS: actual much greater than expected")
    void shouldPassWhenActualIsMuchGreater() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 1000, 1, true));
    }

    @Test
    @DisplayName("PASS: double actual greater than int expected")
    void shouldPassWithMixedNumericTypes() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.rate", 5.5, 5, true));
    }

    @Test
    @DisplayName("PASS: string-to-number coercion — actual number > string expected")
    void shouldPassWithStringCoercion() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 100, "50", true));
    }

    @Test
    @DisplayName("FAIL: actual less than expected")
    void shouldFailWhenActualIsLess() {
        assertThatThrownBy(() -> operator.apply("$.score", 45, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("GREATER_THAN failed");
    }

    @Test
    @DisplayName("FAIL: actual equals expected — not strictly greater")
    void shouldFailWhenEqual() {
        assertThatThrownBy(() -> operator.apply("$.score", 50, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("GREATER_THAN failed");
    }

    @Test
    @DisplayName("FAIL: negative numbers — actual less")
    void shouldFailWithNegativeNumbers() {
        assertThatThrownBy(() -> operator.apply("$.temp", -10, -5, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("GREATER_THAN failed");
    }

    @Test
    @DisplayName("PASS: negative numbers — actual greater")
    void shouldPassWithNegativeNumbers() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.temp", -1, -10, true));
    }
}
