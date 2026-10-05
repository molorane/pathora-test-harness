package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.engine.operator.duration.DurationHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDateTime;

/**
 * Operator: {@code DATETIME_EQUALS_WITH_TOLERANCE}
 *
 * <p>Validates that the extracted datetime matches expected datetime within an allowed tolerance interval.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.completedAt",
 *   "Operator": "DATETIME_EQUALS_WITH_TOLERANCE",
 *   "Value": {
 *     "expected": "2025-06-15T12:00:00",
 *     "tolerance": 5,
 *     "unit": "MINUTES"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATETIME_EQUALS_WITH_TOLERANCE
 */
public class DateTimeEqualsWithToleranceEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATETIME_EQUALS_WITH_TOLERANCE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        Map<String, Object> config = AssertionUtils.toMap(expected);
        Object expObj = config.get("expected");
        if (expObj == null) {
            expObj = config.get("value");
        }
        Object tolObj = config.get("tolerance");

        if (expObj == null || tolObj == null) {
            throw new IllegalArgumentException(
                    "DATETIME_EQUALS_WITH_TOLERANCE requires 'expected' (or 'value') and 'tolerance' in Value at " + path);
        }

        long tolerance = DurationHelper.toLong(tolObj);
        ChronoUnit unit = DurationHelper.parseUnit(String.valueOf(config.getOrDefault("unit", "SECONDS")));

        LocalDateTime actualDt = parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime expectedDt = parseDateTime(String.valueOf(expObj), path + " (expected)");

        LocalDateTime minBound = expectedDt.minus(tolerance, unit);
        LocalDateTime maxBound = expectedDt.plus(tolerance, unit);

        if (actualDt.isBefore(minBound) || actualDt.isAfter(maxBound)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATETIME_EQUALS_WITH_TOLERANCE,
                    path,
                    expectedDt + " (±" + tolerance + " " + unit + ")",
                    actualDt,
                    "DATETIME_EQUALS_WITH_TOLERANCE failed at " + path +
                            ". Expected: " + expectedDt + " ±" + tolerance + " " + unit +
                            ", Actual: " + actualDt);
        }
    }
}

