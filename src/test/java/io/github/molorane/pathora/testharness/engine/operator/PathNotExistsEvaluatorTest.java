package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathNotExistsEvaluatorTest {

    private PathNotExistsEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new PathNotExistsEvaluator();
    }

    // ─────────────────────────────────────────────────────────────────
    // PASS cases — path is absent or filter matched nothing
    // ─────────────────────────────────────────────────────────────────

    /**
     * {@code { "JsonPath": "$.optionalField", "Operator": "PATH_NOT_EXISTS" }}
     * <p>Path threw PathNotFoundException (pathExists = false) → should PASS.</p>
     */
    @Test
    @DisplayName("PASS: path is structurally absent (PathNotFoundException)")
    void shouldPassWhenPathDoesNotExist() {
        assertThatNoException().isThrownBy(
                () -> operator.apply("$.optionalField", null, null, false));
    }

    /**
     * {@code { "JsonPath": "$.items[?(@.type == 'EXCLUDED')]", "Operator": "PATH_NOT_EXISTS" }}
     * <p>Filter expression returned an empty list → should PASS.</p>
     */
    @Test
    @DisplayName("PASS: filter expression returned an empty list")
    void shouldPassWhenFilterReturnsEmptyList() {
        assertThatNoException().isThrownBy(
                () -> operator.apply("$.items[?(@.type == 'EXCLUDED')]", Collections.emptyList(), null, true));
    }

    /**
     * Nested path absent → should PASS.
     */
    @Test
    @DisplayName("PASS: nested path is absent")
    void shouldPassWhenNestedPathDoesNotExist() {
        assertThatNoException().isThrownBy(
                () -> operator.apply("$.outputData.nested.field", null, null, false));
    }

    // ─────────────────────────────────────────────────────────────────
    // FAIL cases — path exists and resolves to a non-empty value
    // ─────────────────────────────────────────────────────────────────

    /**
     * {@code { "JsonPath": "$.outputData.status", "Operator": "PATH_NOT_EXISTS" }}
     * <p>Path exists with a string value → should FAIL.</p>
     */
    @Test
    @DisplayName("FAIL: path exists with a string value")
    void shouldFailWhenPathExistsWithString() {
        assertThatThrownBy(() -> operator.apply("$.outputData.status", "APPROVED", null, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("PATH_NOT_EXISTS_FAILED")
                .hasMessageContaining("$.outputData.status");
    }

    /**
     * {@code { "JsonPath": "$.items[?(@.type == 'ACTIVE')]", "Operator": "PATH_NOT_EXISTS" }}
     * <p>Filter expression returned a non-empty list → should FAIL.</p>
     */
    @Test
    @DisplayName("FAIL: filter expression returned matching elements")
    void shouldFailWhenFilterReturnsNonEmptyList() {
        List<Object> matches = Collections.singletonList("some-item");
        assertThatThrownBy(() -> operator.apply("$.items[?(@.type == 'ACTIVE')]", matches, null, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("PATH_NOT_EXISTS_FAILED");
    }

    /**
     * Path exists but holds a null value — structurally present → should FAIL.
     */
    @Test
    @DisplayName("FAIL: path exists but holds a null value (structurally present)")
    void shouldFailWhenPathExistsWithNullValue() {
        assertThatThrownBy(() -> operator.apply("$.nullableField", null, null, true))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("PATH_NOT_EXISTS_FAILED");
    }
}

