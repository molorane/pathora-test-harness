package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsEmptyObjectEvaluatorTest {

    private IsEmptyObjectEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsEmptyObjectEvaluator();
    }

    @Test
    @DisplayName("PASS: empty map {}")
    void shouldPassWhenEmptyObject() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.meta", Collections.emptyMap(), null, true));
    }

    @Test
    @DisplayName("FAIL: non-empty map")
    void shouldFailWhenNonEmptyObject() {
        assertThatThrownBy(() -> operator.apply("$.meta", Map.of("key", "value"), null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_EMPTY_OBJECT failed");
    }
}

