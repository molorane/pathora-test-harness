package io.github.molorane.pathora.testharness.engine.operator.duration;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;

/**
 * Operator: {@code DURATION_EQUALS}
 *
 * <p>Validates that the elapsed duration between timestamps at {@code startPath} and {@code endPath} equals expected amount in given unit.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "DURATION_EQUALS",
 *   "Value": {
 *     "startPath": "$.outputData.startedAt",
 *     "endPath": "$.outputData.completedAt",
 *     "expected": 30,
 *     "unit": "MINUTES"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DURATION_EQUALS
 */
public class DurationEqualsEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DURATION_EQUALS;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {

        Map<String, Object> config = AssertionUtils.toMap(expected);

        String startPath = String.valueOf(config.get("startPath"));
        String endPath = String.valueOf(config.get("endPath"));
        Object rawExpected;
        String keyName;
        if (config.containsKey("expected")) {
            rawExpected = config.get("expected");
            keyName = "expected";
        } else if (config.containsKey("value")) {
            rawExpected = config.get("value");
            keyName = "value";
        } else {
            throw new IllegalArgumentException(
                "DURATION_EQUALS requires missing or non-numeric 'expected' in Value");
        }
        long expectedDuration;
        try {
            if (rawExpected == null) {
                throw new IllegalArgumentException("missing or non-numeric '" + keyName + "'");
            }
            expectedDuration = DurationHelper.toLong(rawExpected);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "DURATION_EQUALS requires valid numeric duration, missing or non-numeric '" + keyName + "': " + rawExpected, e);
        }
        var unit = DurationHelper.parseUnit(String.valueOf(config.get("unit")));

        long actual = DurationHelper.calculateDuration(
            String.valueOf((Object) context.read(startPath)),
            String.valueOf((Object) context.read(endPath)),
            unit, startPath);

        if (actual != expectedDuration) {
            throw new HarnessAssertionException(
                AssertionOperator.DURATION_EQUALS,
                startPath + " → " + endPath,
                expectedDuration + " " + unit,
                actual + " " + unit,
                "DURATION_EQUALS failed. Expected " + expectedDuration +
                    " " + unit + " but was " + actual + " " + unit);
        }
    }
}
