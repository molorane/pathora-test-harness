package za.co.pathora.testharness.engine.operator;

import com.jayway.jsonpath.DocumentContext;

/**
 * Extended interface for operators that need access to the full JSON response
 * to resolve multiple paths (e.g., FIELD_EQUALS_OTHER_FIELD).
 */
public interface DocumentContextAwareOperator extends OperatorAssertion {

    void apply(DocumentContext context, Object expected);

    @Override
    default void apply(String path, Object actual, Object expected, boolean pathExists) {
        throw new UnsupportedOperationException(
                "This operator requires a DocumentContext. Use apply(DocumentContext, Object) instead.");
    }
}
