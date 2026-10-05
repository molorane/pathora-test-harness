package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalTime;

/**
 * Operator: {@code TIME_EQUALS}
 *
 * <p>Validates that the extracted time strictly equals the expected time (supporting HH:mm and HH:mm:ss precision).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.scheduledTime",
 *   "Operator": "TIME_EQUALS",
 *   "Value": "14:30:00"
 * }
 * }</pre>
 *
 * @see AssertionOperator#TIME_EQUALS
 */
public class TimeEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.TIME_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalTime actualTime = DateUtils.parseTimeOrDateTime(normalizedActual, path);
        String expectedStr = String.valueOf(expected).trim();
        LocalTime expectedTime = DateUtils.parseTime(expectedStr, path + " (expected)");

        boolean match;
        if (hasOnlyHoursAndMinutes(expectedStr)) {
            match = actualTime.getHour() == expectedTime.getHour()
                    && actualTime.getMinute() == expectedTime.getMinute();
        } else {
            match = actualTime.getHour() == expectedTime.getHour()
                    && actualTime.getMinute() == expectedTime.getMinute()
                    && actualTime.getSecond() == expectedTime.getSecond();
        }

        if (!match) {
            throw new HarnessAssertionException(
                    AssertionOperator.TIME_EQUALS,
                    path,
                    expected,
                    actualTime,
                    "TIME_EQUALS failed at " + path +
                            ". Expected time: " + expected +
                            ", Actual time: " + actualTime + " (from " + normalizedActual + ")");
        }
    }

    private boolean hasOnlyHoursAndMinutes(String str) {
        int firstColon = str.indexOf(':');
        return firstColon >= 0 && str.indexOf(':', firstColon + 1) == -1;
    }
}

