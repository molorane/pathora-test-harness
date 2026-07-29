package io.github.molorane.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExistsOperatorTest {

    private PathExistsOperator operator;

    @BeforeEach
    void setUp() {
        operator = new PathExistsOperator();
    }

    /**
     * {@code { "JsonPath": "$.referenceId", "Operator": "PATH_EXISTS" }}
     */
    @Test
    @DisplayName("PASS: path exists with string value")
    void shouldPassWhenPathExistsWithString() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.referenceId", "ABC123", null, true));
    }

    /**
     * {@code { "JsonPath": "$.count", "Operator": "PATH_EXISTS" }}
     */
    @Test
    @DisplayName("PASS: path exists with numeric value")
    void shouldPassWhenPathExistsWithNumber() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.count", 42, null, true));
    }

    /**
     * {@code { "JsonPath": "$.field", "Operator": "PATH_EXISTS" }}
     */
    @Test
    @DisplayName("PASS: path exists with null value")
    void shouldPassWhenPathExistsWithNull() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.field", null, null, true));
    }

    /**
     * {@code { "JsonPath": "$.active", "Operator": "PATH_EXISTS" }}
     */
    @Test
    @DisplayName("PASS: path exists with boolean value")
    void shouldPassWhenPathExistsWithBoolean() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.active", true, null, true));
    }

    /**
     * {@code { "JsonPath": "$.missing", "Operator": "PATH_EXISTS" }}
     */
    @Test
    @DisplayName("FAIL: path does not exist")
    void shouldFailWhenPathDoesNotExist() {
        assertThatThrownBy(() -> operator.apply("$.missing", null, null, false))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("PATH_EXISTS_FAILED")
                .hasMessageContaining("$.missing");
    }

    /**
     * {@code { "JsonPath": "$.outputData.nested.field", "Operator": "PATH_EXISTS" }}
     */
    @Test
    @DisplayName("FAIL: nested path does not exist")
    void shouldFailForNestedMissingPath() {
        assertThatThrownBy(() -> operator.apply("$.outputData.nested.field", null, null, false))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("PATH_EXISTS_FAILED");
    }
}
