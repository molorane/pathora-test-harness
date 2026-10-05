package io.github.molorane.pathora.testharness.engine.operator.scalar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsDecimalEvaluatorTest {

    private IsDecimalEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsDecimalEvaluator();
    }

    @Test
    @DisplayName("PASS: double, float, BigDecimal, decimal string")
    void shouldPassWhenDecimal() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 12.34, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 12.34f, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", new BigDecimal("12.34"), null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", "12.34", null, true));
    }

    @Test
    @DisplayName("FAIL: non-decimal string or non-numeric")
    void shouldFailWhenNotDecimal() {
        assertThatThrownBy(() -> operator.apply("$.val", "abc", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_DECIMAL failed");
    }
}

