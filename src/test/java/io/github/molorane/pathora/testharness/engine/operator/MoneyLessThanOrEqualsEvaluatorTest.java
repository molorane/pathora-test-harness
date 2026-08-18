package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyLessThanOrEqualsEvaluatorTest {

    private MoneyLessThanOrEqualsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new MoneyLessThanOrEqualsEvaluator();
    }

    @Test
    @DisplayName("PASS: money less than or equal")
    void shouldPassWhenLessThanOrEqual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "100.50", "100.50", true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "100.45", "100.50", true));
    }

    @Test
    @DisplayName("FAIL: money strictly greater than")
    void shouldFailWhenGreaterThan() {
        assertThatThrownBy(() -> operator.apply("$.amount", "100.51", "100.50", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_LESS_THAN_OR_EQUALS failed");
    }
}
