package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.engine.operator.duration.DurationHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;

/**
 * Operator: {@code IS_FUTURE_DATETIME}
 *
 * <p>Validates that the extracted datetime timestamp is strictly in the future relative to current datetime (now).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.sessionExpiry",
 *   "Operator": "IS_FUTURE_DATETIME"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_FUTURE_DATETIME
 */
public class IsFutureDateTimeEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_FUTURE_DATETIME;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = DurationHelper.parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime now = LocalDateTime.now();

        if (!actualDt.isAfter(now)) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_FUTURE_DATETIME,
                    path,
                    "after " + now,
                    actualDt,
                    "IS_FUTURE_DATETIME failed at " + path +
                            ". Value " + actualDt + " is not after now (" + now + ")");
        }
    }
}
