package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsUuidEvaluatorTest {

    private IsUuidEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsUuidEvaluator();
    }

    @Test
    @DisplayName("PASS: valid UUID string")
    void shouldPassWhenValidUuid() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.id", "123e4567-e89b-12d3-a456-426614174000", null, true));
    }

    @Test
    @DisplayName("FAIL: invalid UUID string")
    void shouldFailWhenInvalidUuid() {
        assertThatThrownBy(() -> operator.apply("$.id", "invalid-uuid", null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_UUID failed");
    }
}

