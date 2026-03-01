package za.co.pathora.testharness.exception;

import za.co.pathora.testharness.model.AssertionOperator;

public class HarnessAssertionException extends AssertionError {

    public HarnessAssertionException(
            AssertionOperator assertionOperator,
            String path,
            Object expected,
            Object actual,
            String message
    ) {
        super("""
                
                Assertion: %s
                Path:      %s
                Expected:  %s
                Actual:    %s
                
                %s
                """.formatted(assertionOperator, path, expected, actual, message)
        );
    }
}
