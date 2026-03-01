package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LessThanOrEqualsOperatorTest {

    private LessThanOrEqualsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new LessThanOrEqualsOperator();
    }

    @Test
    @DisplayName("PASS: actual less than expected")
    void shouldPassWhenLess() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 30, 50, true));
    }

    @Test
    @DisplayName("PASS: actual equals expected")
    void shouldPassWhenEqual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.score", 50, 50, true));
    }

    @Test
    @DisplayName("PASS: double actual <= int expected")
    void shouldPassWithMixedTypes() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.rate", 5.0, 5, true));
    }

    @Test
    @DisplayName("PASS: negative numbers — actual <= expected")
    void shouldPassWithNegatives() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.temp", -10, -5, true));
    }

    @Test
    @DisplayName("FAIL: actual greater than expected")
    void shouldFailWhenGreater() {
        assertThatThrownBy(() -> operator.apply("$.score", 60, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LESS_THAN_OR_EQUALS failed");
    }

    @Test
    @DisplayName("FAIL: just above expected")
    void shouldFailWhenJustAbove() {
        assertThatThrownBy(() -> operator.apply("$.score", 50.01, 50, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("LESS_THAN_OR_EQUALS failed");
    }
}
