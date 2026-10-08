package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoesNotHaveKeysEvaluatorTest {

    private DoesNotHaveKeysEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new DoesNotHaveKeysEvaluator();
    }

    @Test
    @DisplayName("PASS: object does not contain specified keys")
    void shouldPassWhenKeysAbsent() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.meta", Map.of("id", 1, "name", "test"), List.of("secret", "password"), true));
    }

    @Test
    @DisplayName("FAIL: object contains one or more specified keys")
    void shouldFailWhenKeysPresent() {
        assertThatThrownBy(() ->
            operator.apply("$.meta", Map.of("id", 1, "secret", "xyz"), List.of("secret", "password"), true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("DOES_NOT_HAVE_KEYS failed");
    }
}

