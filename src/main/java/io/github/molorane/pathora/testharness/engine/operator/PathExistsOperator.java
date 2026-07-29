package io.github.molorane.pathora.testharness.engine.operator;

/**
 * Operator: {@code PATH_EXISTS}
 *
 * <p>Asserts that the given JSONPath resolves to a value in the response.
 * The assertion passes regardless of what the value is — including {@code null} —
 * as long as the path is structurally present in the JSON document.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.referenceId",
 *   "Operator": "PATH_EXISTS"
 * }
 * }</pre>
 *
 * @see PathNotExistsOperator
 */
public class PathExistsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        if (!pathExists) {
            throw new AssertionError("""
                    PATH_EXISTS_FAILED
                    Expected path to exist, but it was not found in the response.
                    JsonPath: %s
                    """.formatted(path));
        }
    }
}

