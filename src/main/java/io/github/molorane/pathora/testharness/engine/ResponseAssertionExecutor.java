package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.model.RuleTestCase;

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