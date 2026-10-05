package io.github.molorane.pathora.testharness.engine.operator.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsNotEmptyObjectEvaluatorTest {

    private IsNotEmptyObjectEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsNotEmptyObjectEvaluator();
    }

    @Test
    @DisplayName("PASS: non-empty map")
    void shouldPassWhenNonEmptyObject() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.meta", Map.of("key", "value"), null, true));
    }

    @Test
    @DisplayName("FAIL: empty map {}")
    void shouldFailWhenEmptyObject() {
        assertThatThrownBy(() -> operator.apply("$.meta", Collections.emptyMap(), null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_NOT_EMPTY_OBJECT failed");
    }
}

