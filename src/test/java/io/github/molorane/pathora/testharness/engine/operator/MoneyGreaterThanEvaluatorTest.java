package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyGreaterThanEvaluatorTest {

    private MoneyGreaterThanEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new MoneyGreaterThanEvaluator();
    }

    @Test
    @DisplayName("PASS: money greater than expected")
    void shouldPassWhenActualIsGreaterThanExpected() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.amount", "100.51", "100.50", true));
    }

    @Test
    @DisplayName("FAIL: money equal or less than expected")
    void shouldFailWhenActualIsNotGreaterThanExpected() {
        assertThatThrownBy(() -> operator.apply("$.amount", "100.50", "100.50", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("MONEY_GREATER_THAN failed");
    }
}
