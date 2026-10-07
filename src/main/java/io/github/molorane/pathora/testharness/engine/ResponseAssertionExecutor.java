package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.model.RuleTestCase;

/**
 * Runner that delegates response validation to the configured {@link AssertionEngine}.
 */
public class ResponseAssertionExecutor {

    private final AssertionEngine assertionEngine;

    /**
     * Constructs a new {@code ResponseAssertionExecutor}.
     *
     * @param assertionEngine the assertion engine to delegate to
     */
    public ResponseAssertionExecutor(AssertionEngine assertionEngine) {
        this.assertionEngine = assertionEngine;
    }

    /**
     * Executes test assertions against the response payload.
     *
     * @param mutatedRequest the mutated request that produced the response
     * @param response       the response payload string
     * @param testCase       the test case definition
     */
    public void execute(
        String mutatedRequest,
        String response,
        RuleTestCase testCase) {
        assertionEngine.assertResponse(response, testCase, mutatedRequest);
    }
}

