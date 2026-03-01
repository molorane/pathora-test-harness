package za.co.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonAssertion(

    @JsonProperty("JsonPath")
    String jsonPath,

    @JsonProperty("Operator")
    AssertionOperator operator,

    @JsonProperty("Value")
    Object value
) {
    public JsonAssertion {
        if (operator == null) {
            operator = AssertionOperator.EQUALS;
        }
    }
}