package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringStartsWithAnyEvaluatorTest {

    private StringStartsWithAnyEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringStartsWithAnyEvaluator();
    }

    @Test
    @DisplayName("PASS: string starts with one of the prefixes")
    void shouldPassWhenStartsWithAnyPrefix() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.code", "REF-1234", List.of("INV-", "REF-", "ORD-"), true));
    }

    @Test
    @DisplayName("PASS: string starts with single prefix")
    void shouldPassWithSinglePrefix() {
        assertThatNoException().isThrownBy(() ->
                operator.apply("$.code", "REF-1234", "REF-", true));
    }

    @Test
    @DisplayName("FAIL: string does not start with any prefix")
    void shouldFailWhenDoesNotStartWithAny() {
        assertThatThrownBy(() ->
                operator.apply("$.code", "CUST-1234", List.of("INV-", "REF-", "ORD-"), true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("STRING_STARTS_WITH_ANY failed");
    }
}

