package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsNoneStringBlankEvaluatorTest {

    private IsNoneStringBlankEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsNoneStringBlankEvaluator();
    }

    @Test
    @DisplayName("PASS: all strings in list are non-blank")
    void shouldPassWhenNoneBlank() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.list", List.of("a", "b", "c"), null, true));
    }

    @Test
    @DisplayName("FAIL: at least one string is blank")
    void shouldFailWhenAnyBlank() {
        assertThatThrownBy(() -> operator.apply("$.list", List.of("a", "  ", "c"), null, true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("IS_NONE_STRING_BLANK failed");
    }
}

