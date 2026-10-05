package io.github.molorane.pathora.testharness.engine.operator.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnyMatchEvaluatorTest {

    private AnyMatchEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new AnyMatchEvaluator();
    }

    @Test
    @DisplayName("PASS: at least one element equals expected")
    void shouldPassWhenAnyEquals() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.scores", List.of(10, 20, 30), 20, true));
    }

    @Test
    @DisplayName("PASS: at least one element satisfies condition")
    void shouldPassWhenAnyConditionMatches() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.scores", List.of(10, 20, 30), Map.of("greaterThan", 25), true));
    }

    @Test
    @DisplayName("FAIL: no elements match")
    void shouldFailWhenNoneMatches() {
        assertThatThrownBy(() -> operator.apply("$.scores", List.of(10, 20, 30), 99, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("ANY_MATCH failed");
    }
}

