package io.github.molorane.pathora.testharness.engine.operator.object;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Operator: {@code DOES_NOT_HAVE_KEYS}
 *
 * <p>Validates that the target JSON object does not contain any of the specified prohibited keys.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.user",
 *   "Operator": "DOES_NOT_HAVE_KEYS",
 *   "Value": ["password", "ssn", "secretKey"]
 * }
 * }</pre>
 *
 * @see AssertionOperator#DOES_NOT_HAVE_KEYS
 */
public class DoesNotHaveKeysEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DOES_NOT_HAVE_KEYS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Map<String, Object> map = AssertionUtils.toMap(actual);
        List<String> expectedKeys = toKeyList(expected, path);

        List<String> foundKeys = new ArrayList<>();
        for (String key : expectedKeys) {
            if (map.containsKey(key)) {
                foundKeys.add(key);
            }
        }

        if (!foundKeys.isEmpty()) {
            throw new HarnessAssertionException(
                    AssertionOperator.DOES_NOT_HAVE_KEYS,
                    path,
                    "keys absent: " + expectedKeys,
                    map.keySet(),
                    "DOES_NOT_HAVE_KEYS failed at " + path +
                            ". Found disallowed key(s): " + foundKeys + " in: " + map.keySet());
        }
    }

    private List<String> toKeyList(Object expected, String path) {
        if (expected instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        if (expected instanceof String str) {
            return List.of(str);
        }
        throw new IllegalArgumentException(
                "DOES_NOT_HAVE_KEYS requires key name (String) or List of keys at " + path);
    }
}

