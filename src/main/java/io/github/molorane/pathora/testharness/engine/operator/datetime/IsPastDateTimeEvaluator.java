package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.engine.operator.duration.DurationHelper;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;

/**
 * Operator: {@code IS_PAST_DATETIME}
 *
 * <p>Validates that the extracted datetime timestamp is strictly in the past relative to current datetime (now).</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.lastAccessedAt",
 *   "Operator": "IS_PAST_DATETIME"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_PAST_DATETIME
 */
public class IsPastDateTimeEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_PAST_DATETIME;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = DurationHelper.parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime now = LocalDateTime.now();

        if (!actualDt.isBefore(now)) {
            throw new HarnessAssertionException(
                    AssertionOperator.IS_PAST_DATETIME,
                    path,
                    "before " + now,
                    actualDt,
                    "IS_PAST_DATETIME failed at " + path +
                            ". Value " + actualDt + " is not before now (" + now + ")");
        }
    }
}
