package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringEqualsAnyEvaluatorTest {

    private StringEqualsAnyEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringEqualsAnyEvaluator();
    }

    @Test
    @DisplayName("PASS: string equals one of expected values")
    void shouldPassWhenEqualsAny() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.status", "ACTIVE", List.of("PENDING", "ACTIVE", "CLOSED"), true));
    }

    @Test
    @DisplayName("FAIL: string does not equal any expected value")
    void shouldFailWhenNotEqualsAny() {
        assertThatThrownBy(() ->
                operator.apply("$.status", "UNKNOWN", List.of("PENDING", "ACTIVE", "CLOSED"), true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_EQUALS_ANY failed");
    }

    @Test
    @DisplayName("FAIL: case sensitive check")
    void shouldFailOnCaseMismatch() {
        assertThatThrownBy(() ->
                operator.apply("$.status", "active", List.of("PENDING", "ACTIVE", "CLOSED"), true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_EQUALS_ANY failed");
    }
}

