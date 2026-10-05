package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalTime;

/**
 * Operator: {@code DATE_HOUR_EQUALS}
 *
 * <p>Validates that the hour component (0-23) of the extracted time or datetime matches the expected hour.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.cutoffTime",
 *   "Operator": "DATE_HOUR_EQUALS",
 *   "Value": 17
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_HOUR_EQUALS
 */
public class DateHourEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_HOUR_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalTime actualTime = DateUtils.parseTimeOrDateTime(normalizedActual, path);
        int expectedHour = DateUtils.parseHour(expected, path + " (expected)");
        int actualHour = actualTime.getHour();

        if (actualHour != expectedHour) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_HOUR_EQUALS,
                    path,
                    expectedHour,
                    actualHour,
                    "DATE_HOUR_EQUALS failed at " + path +
                            ". Expected hour: " + expectedHour +
                            ", Actual hour: " + actualHour + " (from " + normalizedActual + ")");
        }
    }
}

