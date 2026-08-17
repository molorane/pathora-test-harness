package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.model.AssertionOperator;
import java.util.Collection;

/**
 * Operator: {@code PATH_NOT_EXISTS}
 *
 * <p>Asserts that the given JSONPath does <strong>not</strong> resolve to any value in the response.
 * This covers two complementary cases:</p>
 * <ul>
 *   <li>The path throws a {@code PathNotFoundException} — the node is structurally absent.</li>
 *   <li>The path evaluates successfully but returns an <em>empty collection</em> —
 *       this typically occurs with JSONPath filter expressions (e.g., {@code [?(@.type == 'X')]})
 *       where no elements match the predicate.</li>
 * </ul>
 *
 * <p>The operator fails if the path resolves to a non-null, non-empty value.</p>
 *
 * <h2>Usage Examples</h2>
 *
 * <p><b>Assert path is structurally absent:</b></p>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.optionalField",
 *   "Operator": "PATH_NOT_EXISTS"
 * }
 * }</pre>
 *
 * <p><b>Assert no array element matches a filter:</b></p>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.items[?(@.type == 'EXCLUDED')]",
 *   "Operator": "PATH_NOT_EXISTS"
 * }
 * }</pre>
 *
 * @see PathExistsOperator
 */
public class PathNotExistsOperator implements OperatorAssertion {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.PATH_NOT_EXISTS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        // Case 1: path threw PathNotFoundException — the path is absent.
        if (!pathExists) {
            return; // Pass — path does not exist.
        }

        // Case 2: path resolved but returned an empty collection.
        // This happens with JSONPath filter expressions that match no elements.
        if (actual instanceof Collection<?> collection && collection.isEmpty()) {
            return; // Pass — filter matched nothing.
        }

        // Case 3: path exists and returned a non-empty value — assertion fails.
        throw new AssertionError("""
                PATH_NOT_EXISTS_FAILED
                Expected path to NOT exist or return no results, but it resolved to a value.
                JsonPath  : %s
                Actual    : %s
                """.formatted(path, actual));
    }
}

