package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TestSuite(

        @JsonProperty("DefaultJSONRequestPath")
        String defaultJSONRequestPath,

        @JsonProperty("Tests")
        List<RuleTestCase> tests
) {
}
