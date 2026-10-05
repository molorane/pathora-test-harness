package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoneMatchEvaluatorTest {

    private NoneMatchEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new NoneMatchEvaluator();
    }

    @Test
    @DisplayName("PASS: no elements match expected value")
    void shouldPassWhenNoneEquals() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.scores", List.of(10, 20, 30), 99, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.scores", Collections.emptyList(), 99, true));
    }

    @Test
    @DisplayName("PASS: no elements satisfy condition")
    void shouldPassWhenNoneConditionMatches() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.scores", List.of(10, 20, 30), Map.of("greaterThan", 50), true));
    }

    @Test
    @DisplayName("FAIL: an element matches expected value")
    void shouldFailWhenAnyMatches() {
        assertThatThrownBy(() -> operator.apply("$.scores", List.of(10, 20, 30), 20, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("NONE_MATCH failed");
    }
}

