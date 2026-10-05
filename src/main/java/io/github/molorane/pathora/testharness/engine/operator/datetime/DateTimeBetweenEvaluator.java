package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDateTime;

/**
 * Operator: {@code DATETIME_BETWEEN}
 *
 * <p>Validates that the extracted datetime timestamp is chronologically between {@code min} and {@code max} inclusive.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.eventTimestamp",
 *   "Operator": "DATETIME_BETWEEN",
 *   "Value": {
 *     "min": "2025-01-01T00:00:00",
 *     "max": "2025-12-31T23:59:59"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATETIME_BETWEEN
 */
public class DateTimeBetweenEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATETIME_BETWEEN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = parseDateTime(String.valueOf(normalizedActual), path);

        Map<String, Object> range = AssertionUtils.toMap(expected);
        Object minObj = range.get("min");
        Object maxObj = range.get("max");

        if (minObj == null || maxObj == null) {
            throw new IllegalArgumentException(
                    "DATETIME_BETWEEN operator requires 'min' and 'max' in Value at " + path);
        }

        LocalDateTime minDt = parseDateTime(String.valueOf(minObj), path + " (min)");
        LocalDateTime maxDt = parseDateTime(String.valueOf(maxObj), path + " (max)");

        if (actualDt.isBefore(minDt) || actualDt.isAfter(maxDt)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATETIME_BETWEEN,
                    path,
                    "datetime between " + minDt + " and " + maxDt,
                    actualDt,
                    "DATETIME_BETWEEN failed at " + path +
                            ". Expected datetime between " + minDt + " and " + maxDt +
                            ", Actual: " + actualDt);
        }
    }
}
