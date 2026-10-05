package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Strategy interface for evaluating an {@link AssertionOperator} against actual extracted values.
 */
public interface AssertionEvaluator {

    /**
     * Returns the assertion operator handled by this evaluator.
     *
     * @return the supported {@link AssertionOperator}
     */
    AssertionOperator operator();

    /**
     * Evaluates the assertion condition against the actual value extracted from the payload.
     *
     * @param path       the JSONPath expression where the value was extracted
     * @param actual     the actual value extracted from the payload (may be null)
     * @param expected   the expected value or condition specification
     * @param pathExists {@code true} if the path was resolved in the payload, {@code false} otherwise
     * @throws io.github.molorane.pathora.testharness.exception.HarnessAssertionException if the assertion fails
     */
    void apply(String path, Object actual, Object expected, boolean pathExists);
}
