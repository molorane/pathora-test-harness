package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Operator: {@code HAS_KEYS}
 *
 * <p>Validates that the target JSON object contains all specified required keys.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.customer",
 *   "Operator": "HAS_KEYS",
 *   "Value": ["id", "name", "email"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#HAS_KEYS
 */
public class HasKeysEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.HAS_KEYS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Map<String, Object> actualMap = AssertionUtils.toMap(actual);
        List<?> expectedKeys = AssertionUtils.requireList(expected, path + " (expected)");

        List<String> missing = new ArrayList<>();
        for (Object key : expectedKeys) {
            String keyStr = String.valueOf(key);
            if (!actualMap.containsKey(keyStr)) {
                missing.add(keyStr);
            }
        }

        if (!missing.isEmpty()) {
            throw new HarnessAssertionException(
                AssertionOperator.HAS_KEYS,
                path,
                expected,
                actualMap.keySet(),
                "HAS_KEYS failed at " + path +
                    ". Missing keys: " + missing +
                    ", Actual keys: " + actualMap.keySet());
        }
    }
}
