package io.github.molorane.pathora.testharness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a test suite configuration file containing request templates and test cases.
 *
 * @param requestPath    the file path to the default JSON request template
 * @param xmlRequestPath the file path to an optional XML request template
 * @param tests          the list of test cases in this suite
 * @param timezone       optional timezone identifier (e.g. UTC, Africa/Johannesburg) for date evaluation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TestSuite(

    @JsonProperty("requestPath")
    String requestPath,

    @JsonProperty("xmlRequestPath")
    String xmlRequestPath,

    @JsonProperty("tests")
    List<RuleTestCase> tests,

    @JsonProperty("timezone")
    String timezone
) {

    /**
     * Backward-compatible constructor for suites without timezone specified.
     *
     * @param requestPath    the file path to the default JSON request template
     * @param xmlRequestPath the file path to an optional XML request template
     * @param tests          the list of test cases in this suite
     */
    public TestSuite(
        String requestPath,
        String xmlRequestPath,
        List<RuleTestCase> tests
    ) {
        this(requestPath, xmlRequestPath, tests, null);
    }

    /**
     * Resolves the primary request template path, preferring XML path if specified.
     *
     * @return the default request template path
     */
    public String defaultRequestPath() {
        if (xmlRequestPath != null && !xmlRequestPath.isBlank()) {
            return xmlRequestPath;
        }
        return requestPath;
    }

    /**
     * Determines whether the test suite uses an XML request template.
     *
     * @return {@code true} if the request is XML-based, {@code false} otherwise
     */
    public boolean isXmlRequest() {
        if (xmlRequestPath != null && !xmlRequestPath.isBlank()) {
            return true;
        }
        String path = defaultRequestPath();
        return path != null && path.toLowerCase().endsWith(".xml");
    }
}


