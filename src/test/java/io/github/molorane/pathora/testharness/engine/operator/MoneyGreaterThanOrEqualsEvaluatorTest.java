package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyGreaterThanOrEqualsEvaluatorTest {

    private MoneyGreaterThanOrEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new MoneyGreaterThanOrEqualsEvaluator();
    }

    @Test
    @DisplayName("PASS: money greater than or equal")
    void shouldPassWhenGreaterThanOrEqual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "100.50", "100.50", true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "100.55", "100.50", true));
    }

    @Test
    @DisplayName("FAIL: money strictly less than")
    void shouldFailWhenLessThan() {
        assertThatThrownBy(() -> operator.apply("$.amount", "100.49", "100.50", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_GREATER_THAN_OR_EQUALS failed");
    }
}
