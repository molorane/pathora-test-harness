package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AllMatchEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.ALL_MATCH;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        List<?> list = AssertionUtils.requireList(actual, path);

        if (list.isEmpty()) {
            return; // empty array — vacuously true
        }

        if (expected instanceof Map<?, ?>) {
            applyCondition(path, list, AssertionUtils.toMap(expected));
        } else {
            applyEquals(path, list, expected);
        }
    }

    private void applyEquals(String path, List<?> list, Object expected) {
        for (int i = 0; i < list.size(); i++) {
            Object element = list.get(i);
            Object[] normalized = AssertionUtils.normalizeTypes(element, expected);

            if (!Objects.equals(normalized[0], normalized[1])) {
                throw new HarnessAssertionException(
                        AssertionOperator.ALL_MATCH,
                        path,
                        expected,
                        element,
                        "ALL_MATCH failed at " + path +
                                "[" + i + "]. Expected all elements to equal: " + expected +
                                ", but element at index " + i + " was: " + element);
            }
        }
    }

    private void applyCondition(String path, List<?> list, Map<String, Object> condition) {

        if (condition.containsKey("greaterThan")) {
            double threshold = toDouble(condition.get("greaterThan"));

            for (int i = 0; i < list.size(); i++) {
                double value = toDouble(list.get(i));
                if (!(value > threshold)) {
                    throw new HarnessAssertionException(
                            AssertionOperator.ALL_MATCH,
                            path,
                            "all > " + threshold,
                            value,
                            "ALL_MATCH failed at " + path +
                                    "[" + i + "]. Expected all elements > " + threshold +
                                    ", but element at index " + i + " was: " + value);
                }
            }

        } else if (condition.containsKey("lessThan")) {
            double threshold = toDouble(condition.get("lessThan"));

            for (int i = 0; i < list.size(); i++) {
                double value = toDouble(list.get(i));
                if (!(value < threshold)) {
                    throw new HarnessAssertionException(
                            AssertionOperator.ALL_MATCH,
                            path,
                            "all < " + threshold,
                            value,
                            "ALL_MATCH failed at " + path +
                                    "[" + i + "]. Expected all elements < " + threshold +
                                    ", but element at index " + i + " was: " + value);
                }
            }

        } else if (condition.containsKey("between")) {
            Map<String, Object> range = AssertionUtils.toMap(condition.get("between"));
            double min = toDouble(range.get("min"));
            double max = toDouble(range.get("max"));

            for (int i = 0; i < list.size(); i++) {
                double value = toDouble(list.get(i));
                if (value < min || value > max) {
                    throw new HarnessAssertionException(
                            AssertionOperator.ALL_MATCH,
                            path,
                            "all between " + min + " and " + max,
                            value,
                            "ALL_MATCH failed at " + path +
                                    "[" + i + "]. Expected all elements between " + min + " and " + max +
                                    ", but element at index " + i + " was: " + value);
                }
            }

        } else {
            throw new IllegalArgumentException(
                    "ALL_MATCH condition must contain 'greaterThan', 'lessThan', or 'between' at " + path +
                            ". Got: " + condition.keySet());
        }
    }

    private double toDouble(Object value) {
        if (value instanceof Number num) {
            return num.doubleValue();
        }
        if (value instanceof String str) {
            return Double.parseDouble(str);
        }
        throw new IllegalArgumentException(
                "Expected numeric value but got: " + value);
    }
}
