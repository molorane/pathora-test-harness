package za.co.pathora.testharness.engine.operator;

import za.co.pathora.testharness.exception.HarnessAssertionException;
import za.co.pathora.testharness.model.AssertionOperator;
import za.co.pathora.testharness.util.AssertionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HasKeysOperator implements OperatorAssertion {

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
