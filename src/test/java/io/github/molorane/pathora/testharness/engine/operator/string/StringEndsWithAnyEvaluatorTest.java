package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringEndsWithAnyEvaluatorTest {

    private StringEndsWithAnyEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new StringEndsWithAnyEvaluator();
    }

    @Test
    @DisplayName("PASS: string ends with one of the suffixes")
    void shouldPassWhenEndsWithAnySuffix() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.filename", "document.pdf", List.of(".png", ".jpg", ".pdf"), true));
    }

    @Test
    @DisplayName("PASS: string ends with single suffix")
    void shouldPassWithSingleSuffix() {
        assertThatNoException().isThrownBy(() ->
            operator.apply("$.filename", "document.pdf", ".pdf", true));
    }

    @Test
    @DisplayName("FAIL: string does not end with any suffix")
    void shouldFailWhenDoesNotEndWithAny() {
        assertThatThrownBy(() ->
            operator.apply("$.filename", "document.docx", List.of(".png", ".jpg", ".pdf"), true))
            .isInstanceOf(HarnessAssertionException.class)
            .hasMessageContaining("STRING_ENDS_WITH_ANY failed");
    }
}

