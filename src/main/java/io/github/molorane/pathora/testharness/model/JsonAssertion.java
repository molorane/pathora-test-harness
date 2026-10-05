package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a single assertion definition in a test case.
 *
 * @param path        the JSONPath expression pointing to the target field in the response
 * @param operator    the assertion operator to apply (defaults to {@link AssertionOperator#EQUALS} if omitted)
 * @param value       the expected value, threshold, object, or parameters required by the operator
 * @param description optional human-readable description of the assertion's purpose
 * @param assertions  nested assertions used by logical operators ({@code AND}, {@code OR}, {@code NOT})
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonAssertion(
        @JsonProperty("path")
        String path,

        @JsonProperty("operator")
        AssertionOperator operator,

        @JsonProperty("value")
        Object value,

        @JsonProperty("description")
        String description,

        @JsonProperty("assertions")
        List<JsonAssertion> assertions
) {
    /**
     * Compact constructor ensuring default operator is {@link AssertionOperator#EQUALS} when null.
     *
     * @param path        the JSONPath expression pointing to the target field in the response
     * @param operator    the assertion operator to apply (defaults to {@link AssertionOperator#EQUALS} if omitted)
     * @param value       the expected value, threshold, object, or parameters required by the operator
     * @param description optional human-readable description of the assertion's purpose
     * @param assertions  nested assertions used by logical operators ({@code AND}, {@code OR}, {@code NOT})
     */
    public JsonAssertion {
        if (operator == null) {
            operator = AssertionOperator.EQUALS;
        }
    }
}

