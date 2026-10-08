package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringEqualsAnyIgnoreCaseEvaluatorTest {

    private StringEqualsAnyIgnoreCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringEqualsAnyIgnoreCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: string equals one of expected values ignoring case")
    void shouldPassWhenEqualsAnyIgnoreCase() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.status", "active", List.of("PENDING", "ACTIVE", "CLOSED"), true));
    }

    @Test
    @DisplayName("FAIL: string does not equal any expected value")
    void shouldFailWhenNotEqualsAny() {
        assertThatThrownBy(() ->
            operator.apply("$.status", "rejected", List.of("PENDING", "ACTIVE", "CLOSED"), true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_EQUALS_ANY_IGNORE_CASE failed");
    }
}

