package io.github.molorane.pathora.testharness.engine.operator.list;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Operator: {@code NONE_MATCH}
 *
 * <p>Validates that no element in the extracted list matches the expected value or numeric predicate condition.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.errors",
 *   "Operator": "NONE_MATCH",
 *   "Value": "CRITICAL"
 * }
 * }</pre>
 *
 * @see AssertionOperator#NONE_MATCH
 */
public class NoneMatchEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.NONE_MATCH;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        List<?> list = AssertionUtils.requireList(actual, path);

        if (list.isEmpty()) {
            return; // vacuously true
        }

        if (expected instanceof Map<?, ?> map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> condition = (Map<String, Object>) map;
            checkNoneCondition(path, list, condition);
        } else {
            for (int i = 0; i < list.size(); i++) {
                Object element = list.get(i);
                Object[] normalized = AssertionUtils.normalizeTypes(element, expected);
                if (Objects.equals(normalized[0], normalized[1])) {
                    throw new HarnessAssertionException(
                            AssertionOperator.NONE_MATCH,
                            path,
                            "none equal to " + expected,
                            element,
                            "NONE_MATCH failed at " + path +
                                    "[" + i + "]. Expected no element to equal: " + expected +
                                    ", but found matching element at index " + i + ": " + element);
                }
            }
        }
    }

    private void checkNoneCondition(String path, List<?> list, Map<String, Object> condition) {
        if (condition.containsKey("greaterThan")) {
            double threshold = toDouble(condition.get("greaterThan"));
            for (int i = 0; i < list.size(); i++) {
                double val = toDouble(list.get(i));
                if (val > threshold) {
                    throw new HarnessAssertionException(
                            AssertionOperator.NONE_MATCH,
                            path,
                            "none > " + threshold,
                            val,
                            "NONE_MATCH failed at " + path +
                                    "[" + i + "]. Expected no elements > " + threshold +
                                    ", but element at index " + i + " was: " + val);
                }
            }
        } else if (condition.containsKey("lessThan")) {
            double threshold = toDouble(condition.get("lessThan"));
            for (int i = 0; i < list.size(); i++) {
                double val = toDouble(list.get(i));
                if (val < threshold) {
                    throw new HarnessAssertionException(
                            AssertionOperator.NONE_MATCH,
                            path,
                            "none < " + threshold,
                            val,
                            "NONE_MATCH failed at " + path +
                                    "[" + i + "]. Expected no elements < " + threshold +
                                    ", but element at index " + i + " was: " + val);
                }
            }
        } else if (condition.containsKey("between")) {
            Map<String, Object> range = AssertionUtils.toMap(condition.get("between"));
            double min = toDouble(range.get("min"));
            double max = toDouble(range.get("max"));
            for (int i = 0; i < list.size(); i++) {
                double val = toDouble(list.get(i));
                if (val >= min && val <= max) {
                    throw new HarnessAssertionException(
                            AssertionOperator.NONE_MATCH,
                            path,
                            "none between " + min + " and " + max,
                            val,
                            "NONE_MATCH failed at " + path +
                                    "[" + i + "]. Expected no elements between " + min + " and " + max +
                                    ", but element at index " + i + " was: " + val);
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "NONE_MATCH condition must contain 'greaterThan', 'lessThan', or 'between' at " + path +
                            ". Got: " + condition.keySet());
        }
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

