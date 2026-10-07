package io.github.molorane.pathora.testharness.engine.operator.duration;

import com.jayway.jsonpath.DocumentContext;
import io.github.molorane.pathora.testharness.engine.operator.DocumentContextAwareEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.Map;

/**
 * Operator: {@code DURATION_LESS_THAN}
 *
 * <p>Validates that the elapsed duration between timestamps at {@code startPath} and {@code endPath} is strictly less than threshold.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "Operator": "DURATION_LESS_THAN",
 *   "Value": {
 *     "startPath": "$.outputData.startedAt",
 *     "endPath": "$.outputData.completedAt",
 *     "value": 60,
 *     "unit": "SECONDS"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DURATION_LESS_THAN
 */
public class DurationLessThanEvaluator implements DocumentContextAwareEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DURATION_LESS_THAN;
    }

    @Override
    public void apply(DocumentContext context, Object expected) {

        Map<String, Object> config = AssertionUtils.toMap(expected);

        String startPath = String.valueOf(config.get("startPath"));
        String endPath = String.valueOf(config.get("endPath"));
        long threshold = DurationHelper.toLong(config.get("value"));
        var unit = DurationHelper.parseUnit(String.valueOf(config.get("unit")));

        long actual = DurationHelper.calculateDuration(
            String.valueOf((Object) context.read(startPath)),
            String.valueOf((Object) context.read(endPath)),
            unit, startPath);

        if (actual >= threshold) {
            throw new HarnessAssertionException(
                AssertionOperator.DURATION_LESS_THAN,
                startPath + " → " + endPath,
                "< " + threshold + " " + unit,
                actual + " " + unit,
                "DURATION_LESS_THAN failed. Expected < " + threshold +
                    " " + unit + " but was " + actual + " " + unit);
        }
    }
}
