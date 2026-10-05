package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringDoesNotContainEvaluatorTest {

    private StringDoesNotContainEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringDoesNotContainEvaluator();
    }

    @Test
    @DisplayName("PASS: string does not contain substring")
    void shouldPassWhenDoesNotContain() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.text", "hello world", "foo", true));
    }

    @Test
    @DisplayName("FAIL: string contains substring")
    void shouldFailWhenContains() {
        assertThatThrownBy(() -> operator.apply("$.text", "hello world", "world", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_DOES_NOT_CONTAIN failed");
    }
}

