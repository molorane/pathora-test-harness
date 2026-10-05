package io.github.molorane.pathora.testharness.engine.operator.scalar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsFalseEvaluatorTest {

    private IsFalseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsFalseEvaluator();
    }

    @Test
    @DisplayName("PASS: boolean false and string false")
    void shouldPassWhenFalse() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", false, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", "false", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", "FALSE", null, true));
    }

    @Test
    @DisplayName("FAIL: boolean true or non-boolean")
    void shouldFailWhenNotFalse() {
        assertThatThrownBy(() -> operator.apply("$.active", true, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_FALSE failed");

        assertThatThrownBy(() -> operator.apply("$.active", "true", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_FALSE failed");
    }
}

