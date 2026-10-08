package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsNumberEvaluatorTest {

    private IsNumberEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsNumberEvaluator();
    }

    @Test
    @DisplayName("PASS: integer, double, or numeric string")
    void shouldPassWhenNumber() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 123, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 45.67, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", "-99.5", null, true));
    }

    @Test
    @DisplayName("FAIL: non-numeric string or object")
    void shouldFailWhenNotNumber() {
        assertThatThrownBy(() -> operator.apply("$.val", "abc", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_NUMBER failed");
    }
}

