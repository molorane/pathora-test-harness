package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalTime;

/**
 * Operator: {@code DATE_MINUTE_EQUALS}
 *
 * <p>Validates that the minute component (0-59) of the extracted time or datetime matches the expected minute.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.cutoffTime",
 *   "Operator": "DATE_MINUTE_EQUALS",
 *   "Value": 30
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_MINUTE_EQUALS
 */
public class DateMinuteEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_MINUTE_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalTime actualTime = DateUtils.parseTimeOrDateTime(normalizedActual, path);
        int expectedMinute = DateUtils.parseMinuteOrSecond(expected, "minute", path + " (expected)");
        int actualMinute = actualTime.getMinute();

        if (actualMinute != expectedMinute) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_MINUTE_EQUALS,
                    path,
                    expectedMinute,
                    actualMinute,
                    "DATE_MINUTE_EQUALS failed at " + path +
                            ". Expected minute: " + expectedMinute +
                            ", Actual minute: " + actualMinute + " (from " + normalizedActual + ")");
        }
    }
}

