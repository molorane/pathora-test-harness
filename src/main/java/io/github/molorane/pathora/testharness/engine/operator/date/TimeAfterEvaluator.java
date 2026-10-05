package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalTime;

/**
 * Operator: {@code TIME_AFTER}
 *
 * <p>Validates that the extracted time is chronologically after the expected time.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.processedAt",
 *   "Operator": "TIME_AFTER",
 *   "Value": "09:00:00"
 * }
 * }</pre>
 *
 * @see AssertionOperator#TIME_AFTER
 */
public class TimeAfterEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.TIME_AFTER;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalTime actualTime = DateUtils.parseTimeOrDateTime(normalizedActual, path);
        LocalTime expectedTime = DateUtils.parseTime(String.valueOf(expected), path + " (expected)");

        if (!actualTime.isAfter(expectedTime)) {
            throw new HarnessAssertionException(
                    AssertionOperator.TIME_AFTER,
                    path,
                    expectedTime,
                    actualTime,
                    "TIME_AFTER failed at " + path +
                            ". Expected time after: " + expectedTime +
                            ", Actual time: " + actualTime + " (from " + normalizedActual + ")");
        }
    }
}

