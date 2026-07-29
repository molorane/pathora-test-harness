package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonMutation(

        @JsonProperty("JsonPath")
        String jsonPath,

        @JsonProperty("Value")
        Object value
) {
}