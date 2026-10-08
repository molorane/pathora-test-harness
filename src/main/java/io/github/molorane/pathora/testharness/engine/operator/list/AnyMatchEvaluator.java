package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Operator: {@code ANY_MATCH}
 *
 * <p>Validates that at least one element in the extracted list matches the expected value or numeric predicate condition.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.roles",
 *   "Operator": "ANY_MATCH",
 *   "Value": "ADMIN"
 * }
 * }</pre>
 *
 * @see AssertionOperator#ANY_MATCH
 */
public class AnyMatchEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.ANY_MATCH;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);

        if (list.isEmpty()) {
            throw new HarnessAssertionException(
                AssertionOperator.ANY_MATCH,
                path,
                expected,
                actual,
                "ANY_MATCH failed at " + path + ". List is empty.");
        }

        boolean matched = false;
        if (expected instanceof Map<?, ?> map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> condition = (Map<String, Object>) map;
            matched = matchesCondition(list, condition);
        } else {
            for (Object element : list) {
                Object[] normalized = AssertionUtils.normalizeTypes(element, expected);
                if (Objects.equals(normalized[0], normalized[1])) {
                    matched = true;
                    break;
                }
            }
        }

        if (!matched) {
            throw new HarnessAssertionException(
                AssertionOperator.ANY_MATCH,
                path,
                expected,
                actual,
                "ANY_MATCH failed at " + path +
                    ". Expected at least one element to match: " + expected +
                    ", but found elements: " + list);
        }
    }

    private boolean matchesCondition(List<?> list, Map<String, Object> condition) {
        if (condition.containsKey("greaterThan")) {
            double threshold = toDouble(condition.get("greaterThan"));
            for (Object item : list) {
                if (toDouble(item) > threshold) {
                    return true;
                }
            }
            return false;
        } else if (condition.containsKey("lessThan")) {
            double threshold = toDouble(condition.get("lessThan"));
            for (Object item : list) {
                if (toDouble(item) < threshold) {
                    return true;
                }
            }
            return false;
        } else if (condition.containsKey("between")) {
            Map<String, Object> range = AssertionUtils.toMap(condition.get("between"));
            double min = toDouble(range.get("min"));
            double max = toDouble(range.get("max"));
            for (Object item : list) {
                double val = toDouble(item);
                if (val >= min && val <= max) {
                    return true;
                }
            }
            return false;
        }
        throw new IllegalArgumentException(
            "ANY_MATCH condition must contain 'greaterThan', 'lessThan', or 'between'. Got: " + condition.keySet());
    }

    private double toDouble(Object value) {
        if (value instanceof Number num) {
            return num.doubleValue();
        }
        if (value instanceof String str) {
            return Double.parseDouble(str.trim());
        }
        throw new IllegalArgumentException("Expected numeric value but got: " + value);
    }
}

