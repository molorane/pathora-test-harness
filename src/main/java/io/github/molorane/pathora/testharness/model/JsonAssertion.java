package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a single assertion definition in a test case.
 *
 * @param path        the JSONPath expression pointing to the target field in the response
 * @param operator    the assertion operator name to apply
 * @param value       the expected value, threshold, object, or parameters required by the operator
 * @param description optional human-readable description of the assertion's purpose
 * @param assertions  nested assertions used by logical operators ({@code AND}, {@code OR}, {@code NOT})
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonAssertion(
    @JsonProperty("path")
    String path,

    @JsonProperty("operator")
    String operator,

    @JsonProperty("value")
    Object value,

    @JsonProperty("description")
    String description,

    @JsonProperty("assertions")
    List<JsonAssertion> assertions
) {
    public JsonAssertion(
        String path,
        AssertionOperator operator,
        Object value,
        String description,
        List<JsonAssertion> assertions) {
        this(path, operator == null ? null : operator.name(), value, description, assertions);
    }

    @JsonCreator
    public JsonAssertion(
        @JsonProperty("path") String path,
        @JsonProperty("operator") Object operator,
        @JsonProperty("value") Object value,
        @JsonProperty("description") String description,
        @JsonProperty("assertions") List<JsonAssertion> assertions) {
        this(path, normalizeOperator(operator), value, description, assertions);
    }

    /**
     * Compact constructor ensuring default operator is {@link AssertionOperator#EQUALS} when null.
     */
    public JsonAssertion {
        if (operator == null || operator.isBlank()) {
            operator = AssertionOperator.EQUALS.name();
        }
        operator = operator.trim().toUpperCase();
    }

    /**
     * Resolves the operator name to the core enum if it is a built-in operator.
     *
     * @return the matching {@link AssertionOperator}, or {@code null} if this is a custom operator name
     */
    public AssertionOperator asEnum() {
        try {
            return AssertionOperator.valueOf(operator);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns the normalized operator name used by the registry.
     *
     * @return uppercase operator name
     */
    public String operatorName() {
        return operator;
    }

    private static String normalizeOperator(Object operator) {
        if (operator == null) {
            return null;
        }
        if (operator instanceof AssertionOperator assertionOperator) {
            return assertionOperator.name();
        }
        if (operator instanceof String string) {
            return string.trim();
        }
        return String.valueOf(operator).trim();
    }
}
