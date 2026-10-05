package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsAllStringBlankEvaluatorTest {

    private IsAllStringBlankEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsAllStringBlankEvaluator();
    }

    @Test
    @DisplayName("PASS: all strings in list are blank")
    void shouldPassWhenAllBlank() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.list", List.of(" ", "  ", "\t"), null, true));
    }

    @Test
    @DisplayName("FAIL: at least one string is non-blank")
    void shouldFailWhenNotAllBlank() {
        assertThatThrownBy(() -> operator.apply("$.list", List.of(" ", "hello", " "), null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_ALL_STRING_BLANK failed");
    }
}

