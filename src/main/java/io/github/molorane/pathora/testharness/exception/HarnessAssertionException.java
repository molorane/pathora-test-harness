package io.github.molorane.pathora.testharness.exception;

import io.github.molorane.pathora.testharness.model.AssertionOperator;

public class HarnessAssertionException extends AssertionError {

    private final AssertionOperator operator;
    private final String path;
    private final Object expected;
    private final Object actual;

    public HarnessAssertionException(
            AssertionOperator operator,
            String path,
            Object expected,
            Object actual,
            String message) {
        super(formatMessage(operator, path, expected, actual, message));

        this.operator = operator;
        this.path = path;
        this.expected = expected;
        this.actual = actual;
    }

    private static String formatMessage(
            AssertionOperator operator,
            String path,
            Object expected,
            Object actual,
            String message) {
        return """
                
                Assertion: %s
                Path:      %s
                Expected:  %s
                Actual:    %s
                
                %s
                """.formatted(operator, path, expected, actual, message);
    }

    public AssertionOperator operator() {
        return operator;
    }

    public String path() {
        return path;
    }

    public Object expected() {
        return expected;
    }

    public Object actual() {
        return actual;
    }
}