package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;

/**
 * Operator: {@code IS_NOT_EMPTY_OBJECT}
 *
 * <p>Validates that the target value is a non-empty JSON object containing at least one property.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.profile",
 *   "Operator": "IS_NOT_EMPTY_OBJECT"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_NOT_EMPTY_OBJECT
 */
public class IsNotEmptyObjectEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_NOT_EMPTY_OBJECT;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Map<String, Object> map = AssertionUtils.toMap(actual);

        if (map.isEmpty()) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_NOT_EMPTY_OBJECT,
                path,
                "non-empty object",
                actual,
                "IS_NOT_EMPTY_OBJECT failed at " + path +
                    ". Expected non-empty object but found 0 keys.");
        }
    }
}

