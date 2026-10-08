package io.github.molorane.pathora.testharness.engine.operator.scalar;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsIntegerEvaluatorTest {

    private IsIntegerEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsIntegerEvaluator();
    }

    @Test
    @DisplayName("PASS: integer, long, or whole number string")
    void shouldPassWhenInteger() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 100, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 100L, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", "250", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.val", 100.0, null, true));
    }

    @Test
    @DisplayName("FAIL: decimal or non-numeric")
    void shouldFailWhenNotInteger() {
        assertThatThrownBy(() -> operator.apply("$.val", 100.55, null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_INTEGER failed");

        assertThatThrownBy(() -> operator.apply("$.val", "100.5", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_INTEGER failed");
    }
}

