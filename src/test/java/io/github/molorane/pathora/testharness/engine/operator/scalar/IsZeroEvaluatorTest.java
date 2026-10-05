package io.github.molorane.pathora.testharness.engine.operator.scalar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsZeroEvaluatorTest {

    private IsZeroEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsZeroEvaluator();
    }

    @Test
    @DisplayName("PASS: number == 0")
    void shouldPassWhenZero() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 0, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 0.0, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", "0", null, true));
    }

    @Test
    @DisplayName("FAIL: number != 0")
    void shouldFailWhenNotZero() {
        assertThatThrownBy(() -> operator.apply("$.val", 1, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_ZERO failed");

        assertThatThrownBy(() -> operator.apply("$.val", -0.5, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_ZERO failed");
    }
}

