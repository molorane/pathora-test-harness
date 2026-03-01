package za.co.nedbank.brm.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RuleTestCase(

    @JsonProperty("TestName")
    String testName,

    @JsonProperty("TestDescription")
    String testDescription,

    @JsonProperty("EntryPointName")
    String entryPointName,

    @JsonProperty("TestCaseParameterValues")
    List<JsonMutation> testCaseParameterValues,

    @JsonProperty("ResponseAssertions")
    List<JsonAssertion> responseAssertions
) {
}