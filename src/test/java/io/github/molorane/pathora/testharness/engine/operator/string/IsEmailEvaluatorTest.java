package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsEmailEvaluatorTest {

    private IsEmailEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsEmailEvaluator();
    }

    @Test
    @DisplayName("PASS: valid email string")
    void shouldPassWhenValidEmail() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.email", "user@example.com", null, true));
    }

    @Test
    @DisplayName("FAIL: invalid email string")
    void shouldFailWhenInvalidEmail() {
        assertThatThrownBy(() -> operator.apply("$.email", "not-an-email", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_EMAIL failed");
    }
}

