package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a payload mutation applied to a base request template prior to test execution.
 *
 * @param path  the JSONPath expression pointing to the field to mutate
 * @param value the new value to inject into the request payload at the specified path
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonMutation(

        @JsonProperty("path")
        String path,

        @JsonProperty("value")
        Object value
) {
}

