package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyLessThanEvaluatorTest {

    private MoneyLessThanEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new MoneyLessThanEvaluator();
    }

    @Test
    @DisplayName("PASS: money strictly less than")
    void shouldPassWhenLessThan() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "100.49", "100.50", true));
    }

    @Test
    @DisplayName("FAIL: money greater than or equal")
    void shouldFailWhenGreaterThanOrEqual() {
        assertThatThrownBy(() -> operator.apply("$.amount", "100.50", "100.50", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_LESS_THAN failed");
    }
}
