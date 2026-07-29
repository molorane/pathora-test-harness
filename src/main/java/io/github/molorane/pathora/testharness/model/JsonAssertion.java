package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonAssertion(
        @JsonProperty("JsonPath")
        String jsonPath,

        @JsonProperty("Operator")
        AssertionOperator operator,

        @JsonProperty("Value")
        Object value,

        @JsonProperty("Description")
        String description,

        @JsonProperty("Assertions")
        List<JsonAssertion> assertions
) {
    public JsonAssertion {
        if (operator == null) {
            operator = AssertionOperator.EQUALS;
        }
    }
}