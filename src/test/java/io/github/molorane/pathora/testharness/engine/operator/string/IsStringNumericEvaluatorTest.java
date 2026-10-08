package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsStringNumericEvaluatorTest {

    private IsStringNumericEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsStringNumericEvaluator();
    }

    @Test
    @DisplayName("PASS: digits only")
    void shouldPassWhenNumeric() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "12345", null, true));
    }

    @Test
    @DisplayName("FAIL: contains non-digits")
    void shouldFailWhenNotNumeric() {
        assertThatThrownBy(() -> operator.apply("$.code", "12a45", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_STRING_NUMERIC failed");
    }
}

