package io.github.molorane.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;

/**
 * Extended interface for evaluators that need access to the full JSON response document
 * to resolve multiple paths or complex structures (e.g., cross-field comparisons).
 */
public interface DocumentContextAwareEvaluator extends AssertionEvaluator {

    /**
     * Evaluates the assertion across the entire JSON document context.
     *
     * @param context  the Jayway JsonPath parsed document context
     * @param expected the expected condition or parameters object
     */
    void apply(DocumentContext context, Object expected);

    @Override
    default void apply(String path, Object actual, Object expected, boolean pathExists) {
        throw new UnsupportedOperationException(
                "This evaluator requires a DocumentContext. Use apply(DocumentContext, Object) instead.");
    }
}


