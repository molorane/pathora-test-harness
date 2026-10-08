package io.github.molorane.pathora.testharness.engine.operator.duration;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;

/**
 * Operator: {@code DURATION_BETWEEN}
 *
 * <p>Validates that the elapsed duration between timestamps at {@code startPath} and {@code endPath} is inclusively between {@code min} and {@code max}.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "DURATION_BETWEEN",
 *   "Value": {
 *     "startPath": "$.outputData.startTime",
 *     "endPath": "$.outputData.endTime",
 *     "min": 1,
 *     "max": 5,
 *     "unit": "HOURS"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DURATION_BETWEEN
 */
public class DurationBetweenDatesEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DURATION_BETWEEN;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {

        Map<String, Object> config = AssertionUtils.toMap(expected);

        String startPath = String.valueOf(config.get("startPath"));
        String endPath = String.valueOf(config.get("endPath"));
        long min = DurationHelper.toLong(config.get("min"));
        long max = DurationHelper.toLong(config.get("max"));

        String startStr = String.valueOf((Object) context.read(startPath));
        String endStr = String.valueOf((Object) context.read(endPath));

        long duration = DurationHelper.calculateDuration(startStr, endStr,
            DurationHelper.parseUnit(String.valueOf(config.get("unit"))), startPath);

        if (duration < min || duration > max) {
            throw new HarnessAssertionException(
                AssertionOperator.DURATION_BETWEEN,
                startPath + " → " + endPath,
                "between " + min + " and " + max,
                duration,
                "DURATION_BETWEEN failed. Duration from " +
                    startPath + " (" + startStr + ") to " +
                    endPath + " (" + endStr + ") is " +
                    duration + ". Expected between " + min + " and " + max);
        }
    }
}
