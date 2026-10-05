package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsUrlEvaluatorTest {

    private IsUrlEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsUrlEvaluator();
    }

    @Test
    @DisplayName("PASS: valid URL string")
    void shouldPassWhenValidUrl() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.link", "https://www.example.com/api/v1", null, true));
    }

    @Test
    @DisplayName("FAIL: invalid URL string")
    void shouldFailWhenInvalidUrl() {
        assertThatThrownBy(() -> operator.apply("$.link", "not-a-url", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_URL failed");
    }
}

