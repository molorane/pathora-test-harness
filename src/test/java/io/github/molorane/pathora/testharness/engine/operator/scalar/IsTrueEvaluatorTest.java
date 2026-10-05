package io.github.molorane.pathora.testharness.engine.operator.scalar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsTrueEvaluatorTest {

    private IsTrueEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsTrueEvaluator();
    }

    @Test
    @DisplayName("PASS: boolean true and string true")
    void shouldPassWhenTrue() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", true, null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", "true", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", "TRUE", null, true));
    }

    @Test
    @DisplayName("FAIL: boolean false, string false, or non-boolean")
    void shouldFailWhenNotTrue() {
        assertThatThrownBy(() -> operator.apply("$.active", false, null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_TRUE failed");

        assertThatThrownBy(() -> operator.apply("$.active", "false", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_TRUE failed");

        assertThatThrownBy(() -> operator.apply("$.active", "random", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_TRUE failed");
    }
}

