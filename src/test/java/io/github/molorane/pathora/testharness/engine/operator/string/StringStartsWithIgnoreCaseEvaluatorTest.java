package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringStartsWithIgnoreCaseEvaluatorTest {

    private StringStartsWithIgnoreCaseEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringStartsWithIgnoreCaseEvaluator();
    }

    @Test
    @DisplayName("PASS: string starts with prefix ignoring case")
    void shouldPassWhenStartsWithIgnoreCase() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.refId", "ref-1234", "REF-", true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.refId", "REF-1234", "ref-", true));
    }

    @Test
    @DisplayName("FAIL: string does not start with prefix")
    void shouldFailWhenDoesNotStartWith() {
        assertThatThrownBy(() -> operator.apply("$.refId", "inv-1234", "REF-", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_STARTS_WITH_IGNORE_CASE failed");
    }
}

