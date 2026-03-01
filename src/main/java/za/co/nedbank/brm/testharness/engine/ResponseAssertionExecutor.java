package za.co.nedbank.brm.testharness.engine;


import za.co.nedbank.brm.testharness.model.RuleTestCase;

import java.nio.file.Path;

public class ResponseAssertionExecutor {

    private final AssertionEngine assertionEngine;

    public ResponseAssertionExecutor(AssertionEngine assertionEngine) {
        this.assertionEngine = assertionEngine;
    }

    public void execute(
            Path testFileName,
            String mutatedRequest,
            String response,
            RuleTestCase testCase
    ) {

        assertionEngine.assertResponse(
                testFileName,
                mutatedRequest,
                response,
                testCase
        );
    }
}