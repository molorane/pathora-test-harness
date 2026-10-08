package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalTime;

/**
 * Operator: {@code TIME_BEFORE}
 *
 * <p>Validates that the extracted time is chronologically before the expected time.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.submissionTime",
 *   "Operator": "TIME_BEFORE",
 *   "Value": "17:00:00"
 * }
 * }</pre>
 *
 * @see AssertionOperator#TIME_BEFORE
 */
public class TimeBeforeEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.TIME_BEFORE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalTime actualTime = DateUtils.parseTimeOrDateTime(normalizedActual, path);
        LocalTime expectedTime = DateUtils.parseTime(String.valueOf(expected), path + " (expected)");

        if (!actualTime.isBefore(expectedTime)) {
            throw new HarnessAssertionException(
                AssertionOperator.TIME_BEFORE,
                path,
                expectedTime,
                actualTime,
                "TIME_BEFORE failed at " + path +
                    ". Expected time before: " + expectedTime +
                    ", Actual time: " + actualTime + " (from " + normalizedActual + ")");
        }
    }
}

