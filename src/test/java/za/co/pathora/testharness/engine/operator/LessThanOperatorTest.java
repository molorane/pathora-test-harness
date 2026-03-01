package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LessThanOperatorTest {

    private LessThanOperator operator;

    @BeforeEach
    void setUp() {
        operator = new LessThanOperator();
    }

    @Test
    @DisplayName("PASS: actual less than expected")
    void shouldPassWhenActualIsLess() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 80, 100, true));
    }

    @Test
    @DisplayName("PASS: actual much less than expected")
    void shouldPassWhenActualIsMuchLess() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 1, 1000, true));
    }

    @Test
    @DisplayName("PASS: double actual less than int expected")
    void shouldPassWithMixedNumericTypes() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.rate", 4.9, 5, true));
    }

    @Test
    @DisplayName("PASS: string-to-number coercion")
    void shouldPassWithStringCoercion() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 10, "50", true));
    }

    @Test
    @DisplayName("FAIL: actual greater than expected")
    void shouldFailWhenActualIsGreater() {
        assertThatThrownBy(() -> operator.apply("$.score", 120, 100, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LESS_THAN failed");
    }

    @Test
    @DisplayName("FAIL: actual equals expected — not strictly less")
    void shouldFailWhenEqual() {
        assertThatThrownBy(() -> operator.apply("$.score", 100, 100, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LESS_THAN failed");
    }

    @Test
    @DisplayName("PASS: negative numbers — actual less")
    void shouldPassWithNegativeNumbers() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.temp", -10, -1, true));
    }

    @Test
    @DisplayName("FAIL: negative numbers — actual greater")
    void shouldFailWithNegativeNumbers() {
        assertThatThrownBy(() -> operator.apply("$.temp", -1, -10, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LESS_THAN failed");
    }

    @Test
    @DisplayName("PASS: zero less than positive")
    void shouldPassWhenZeroLessThanPositive() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 0, 1, true));
    }
}
