package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalTime;
import java.util.Map;

/**
 * Operator: {@code TIME_BETWEEN}
 *
 * <p>Validates that the extracted time is chronologically between the specified {@code min} and {@code max} times inclusive.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.operatingHours",
 *   "Operator": "TIME_BETWEEN",
 *   "Value": {
 *     "min": "08:00:00",
 *     "max": "18:00:00"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#TIME_BETWEEN
 */
public class TimeBetweenEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.TIME_BETWEEN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalTime actualTime = DateUtils.parseTimeOrDateTime(normalizedActual, path);

        Map<String, Object> map = AssertionUtils.toMap(expected);
        LocalTime min = DateUtils.parseTime(String.valueOf(map.get("min")), path + ".min");
        LocalTime max = DateUtils.parseTime(String.valueOf(map.get("max")), path + ".max");

        if (actualTime.isBefore(min) || actualTime.isAfter(max)) {
            throw new HarnessAssertionException(
                AssertionOperator.TIME_BETWEEN,
                path,
                min + " - " + max,
                actualTime,
                "TIME_BETWEEN failed at " + path +
                    ". Expected between " + min + " and " + max +
                    ", Actual time: " + actualTime + " (from " + normalizedActual + ")");
        }
    }
}

