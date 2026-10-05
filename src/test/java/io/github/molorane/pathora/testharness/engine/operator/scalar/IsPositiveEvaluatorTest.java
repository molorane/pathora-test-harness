package io.github.molorane.pathora.testharness.engine.operator.scalar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsPositiveEvaluatorTest {

    private IsPositiveEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsPositiveEvaluator();
    }

    @Test
    @DisplayName("PASS: number > 0")
    void shouldPassWhenPositive() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 10, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 0.001, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", "5", null, true));
    }

    @Test
    @DisplayName("FAIL: number <= 0")
    void shouldFailWhenNotPositive() {
        assertThatThrownBy(() -> operator.apply("$.val", 0, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_POSITIVE failed");

        assertThatThrownBy(() -> operator.apply("$.val", -15, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_POSITIVE failed");
    }
}

