package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.engine.operator.duration.DurationHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Operator: {@code DATETIME_WITHIN_LAST}
 *
 * <p>Validates that the extracted datetime timestamp falls within the last specified temporal interval relative to current system datetime (now).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.lastLoginAt",
 *   "Operator": "DATETIME_WITHIN_LAST",
 *   "Value": {
 *     "amount": 24,
 *     "unit": "HOURS"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATETIME_WITHIN_LAST
 */
public class DateTimeWithinLastEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATETIME_WITHIN_LAST;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        Map<String, Object> config = AssertionUtils.toMap(expected);

        long amount = DurationHelper.toLong(config.get("amount"));
        ChronoUnit unit = DurationHelper.parseUnit(String.valueOf(config.get("unit")));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minus(amount, unit);
        LocalDateTime actualDt = DurationHelper.parseDateTime(String.valueOf(normalizedActual), path);

        if (actualDt.isBefore(threshold)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATETIME_WITHIN_LAST,
                    path,
                    "within last " + amount + " " + unit,
                    actualDt,
                    "DATETIME_WITHIN_LAST failed at " + path +
                            ". Value " + actualDt +
                            " is not within the last " + amount + " " + unit +
                            " (threshold: " + threshold + ")");
        }
    }
}
