package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a discrete test case containing mutations to apply to the request and assertions to evaluate on the response.
 *
 * @param name        the unique name or identifier of the test case
 * @param description a human-readable description of what this test case verifies
 * @param operation   the target operation or entry point name to invoke
 * @param mutations   the list of input payload mutations to apply before execution
 * @param assertions  the list of assertions to validate against the response
 * @param timezone    optional timezone identifier for date/datetime evaluation in this test case
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RuleTestCase(

    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description,

    @JsonProperty("operation")
    String operation,

    @JsonProperty("mutations")
    List<JsonMutation> mutations,

    @JsonProperty("assertions")
    List<JsonAssertion> assertions,

    @JsonProperty("timezone")
    String timezone
) {

    /**
     * Backward-compatible constructor without timezone.
     *
     * @param name        the unique name or identifier of the test case
     * @param description a human-readable description of what this test case verifies
     * @param operation   the target operation or entry point name to invoke
     * @param mutations   the list of input payload mutations to apply before execution
     * @param assertions  the list of assertions to validate against the response
     */
    public RuleTestCase(
        String name,
        String description,
        String operation,
        List<JsonMutation> mutations,
        List<JsonAssertion> assertions
    ) {
        this(name, description, operation, mutations, assertions, null);
    }
}

