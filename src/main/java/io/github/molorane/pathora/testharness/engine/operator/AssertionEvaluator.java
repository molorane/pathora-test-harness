package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.model.AssertionOperator;

/**
 * Strategy interface for evaluating an assertion rule against actual extracted values.
 *
 * <p>Built-in operators continue to expose an {@link AssertionOperator}. Plugin authors may
 * provide custom operators by implementing only {@link #operatorName()} and registering the
 * evaluator with {@link io.github.molorane.pathora.testharness.engine.OperatorRegistry}.</p>
 */
public interface AssertionEvaluator {

    /**
     * Returns the assertion operator handled by this evaluator.
     *
     * <p>Built-in evaluators should override this method. Custom evaluators may override
     * {@link #operatorName()} instead, which keeps the plugin contract independent from the
     * core enum.</p>
     *
     * @return the supported {@link AssertionOperator}
     */
    default AssertionOperator operator() {
        throw new UnsupportedOperationException(
            "Custom operators must implement operatorName() and register by string name.");
    }

    /**
     * Returns the operator name used by the registry and JSON assertions.
     *
     * @return the operator name in canonical uppercase form
     */
    default String operatorName() {
        return operator().name();
    }

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
