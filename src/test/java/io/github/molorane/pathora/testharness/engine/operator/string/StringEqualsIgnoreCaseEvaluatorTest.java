package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringEqualsIgnoreCaseEvaluatorTest {

    private StringEqualsIgnoreCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringEqualsIgnoreCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: string equals expected ignoring case")
    void shouldPassWhenEqualsIgnoreCase() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "active", "ACTIVE", true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.status", "SUCCESS", "success", true));
    }

    @Test
    @DisplayName("FAIL: string does not equal expected")
    void shouldFailWhenNotEquals() {
        assertThatThrownBy(() -> operator.apply("$.status", "active", "INACTIVE", true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_EQUALS_IGNORE_CASE failed");
    }
}

