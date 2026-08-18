package io.github.molorane.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;

/**
 * Extended interface for evaluators that need access to the full JSON response
 * to resolve multiple paths (e.g., FIELD_EQUALS_OTHER_FIELD).
 */
public interface DocumentContextAwareEvaluator extends AssertionEvaluator {

    void apply(DocumentContext context, Object expected);

    @Override
    default void apply(String path, Object actual, Object expected, boolean pathExists) {
        throw new UnsupportedOperationException(
                "This evaluator requires a DocumentContext. Use apply(DocumentContext, Object) instead.");
    }
}
