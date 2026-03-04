package za.co.pathora.testharness.engine;

import za.co.pathora.testharness.model.RuleTestCase;

public class ResponseAssertionExecutor {

    private final AssertionEngine assertionEngine;

    public ResponseAssertionExecutor(AssertionEngine assertionEngine) {
        this.assertionEngine = assertionEngine;
    }

    public void execute(
            String response,
            RuleTestCase testCase) {

        assertionEngine.assertResponse(
                response,
                testCase);
    }
}