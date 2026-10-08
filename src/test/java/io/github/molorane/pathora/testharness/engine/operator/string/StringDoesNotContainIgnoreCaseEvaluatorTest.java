package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringDoesNotContainIgnoreCaseEvaluatorTest {

    private StringDoesNotContainIgnoreCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringDoesNotContainIgnoreCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: string does not contain substring ignoring case")
    void shouldPassWhenDoesNotContain() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "hello world", "FOO", true));
    }

    @Test
    @DisplayName("FAIL: string contains substring ignoring case")
    void shouldFailWhenContains() {
        assertThatThrownBy(() -> operator.apply("$.text", "hello world", "WORLD", true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_DOES_NOT_CONTAIN_IGNORE_CASE failed");
    }
}

