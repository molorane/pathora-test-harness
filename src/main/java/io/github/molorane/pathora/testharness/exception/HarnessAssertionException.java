package io.github.molorane.pathora.testharness.exception;

import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Custom {@link AssertionError} thrown when an assertion evaluation fails within the test harness.
 *
 * <p>Captures rich diagnostic context including the failing test name, test description,
 * assertion operator, JSONPath, expected value, and actual extracted value.</p>
 */
public class HarnessAssertionException extends AssertionError {

    /** The name of the failing test case. */
    private final String testName;
    /** The description of the failing test case. */
    private final String testDescription;
    /** The assertion operator that failed. */
    private final AssertionOperator operator;
    /** The JSONPath evaluated. */
    private final String path;
    /** The expected value. */
    private final Object expected;
    /** The actual value extracted from the response. */
    private final Object actual;
    /** The detailed failure message. */
    private final String detailMessage;

    /**
     * Constructs a new {@code HarnessAssertionException} without test name and description context.
     *
     * @param operator the operator that failed
     * @param path     the JSONPath that failed
     * @param expected the expected value
     * @param actual   the actual value
     * @param message  a descriptive failure message
     */
    public HarnessAssertionException(
            AssertionOperator operator,
            String path,
            Object expected,
            Object actual,
            String message) {
        this(null, null, operator, path, expected, actual, message);
    }

    /**
     * Constructs a new {@code HarnessAssertionException} with full test and failure details.
     *
     * @param testName        the name of the test case
     * @param testDescription the description of the test case
     * @param operator        the operator that failed
     * @param path            the JSONPath that failed
     * @param expected        the expected value
     * @param actual          the actual value
     * @param message         a descriptive failure message
     */
    public HarnessAssertionException(
            String testName,
            String testDescription,
            AssertionOperator operator,
            String path,
            Object expected,
            Object actual,
            String message) {
        super(formatMessage(testName, testDescription, operator, path, expected, actual, message));

        this.testName = testName;
        this.testDescription = testDescription;
        this.operator = operator;
        this.path = path;
        this.expected = expected;
        this.actual = actual;
        this.detailMessage = message;
    }

    private static String formatMessage(
            String testName,
            String testDescription,
            AssertionOperator operator,
            String path,
            Object expected,
            Object actual,
            String message) {
        return """
                
                TestName:        %s
                TestDescription: %s
                Assertion:       %s
                Path:            %s
                Expected:        %s
                Actual:          %s
                
                %s
                """.formatted(testName, testDescription, operator, path, expected, actual, message);
    }

    /**
     * Returns the test name.
     *
     * @return the test name
     */
    public String testName() {
        return testName;
    }

    /**
     * Returns the test description.
     *
     * @return the test description
     */
    public String testDescription() {
        return testDescription;
    }

    /**
     * Returns the assertion operator that failed.
     *
     * @return the assertion operator
     */
    public AssertionOperator operator() {
        return operator;
    }

    /**
     * Returns the JSONPath evaluated.
     *
     * @return the JSONPath
     */
    public String path() {
        return path;
    }

    /**
     * Returns the expected value.
     *
     * @return the expected value
     */
    public Object expected() {
        return expected;
    }

    /**
     * Returns the actual value extracted from the response.
     *
     * @return the actual value
     */
    public Object actual() {
        return actual;
    }

    /**
     * Returns the detailed failure message.
     *
     * @return the detail message
     */
    public String detailMessage() {
        return detailMessage;
    }

    /**
     * Creates a new copy of this exception enriched with test name and description context.
     *
     * @param testName        the name of the test case
     * @param testDescription the description of the test case
     * @return a new {@code HarnessAssertionException} containing the test details
     */
    public HarnessAssertionException withTestDetails(String testName, String testDescription) {
        return new HarnessAssertionException(
                testName,
                testDescription,
                this.operator,
                this.path,
                this.expected,
                this.actual,
                this.detailMessage != null ? this.detailMessage : getMessage());
    }
}

