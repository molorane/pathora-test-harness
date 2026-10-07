package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDateTime;

/**
 * Operator: {@code DATETIME_AFTER}
 *
 * <p>Validates that the extracted datetime timestamp is chronologically after the expected datetime.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.updatedAt",
 *   "Operator": "DATETIME_AFTER",
 *   "Value": "2025-01-01T00:00:00"
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATETIME_AFTER
 */
public class DateTimeAfterEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATETIME_AFTER;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime expectedDt = parseDateTime(String.valueOf(expected), path);

        if (!actualDt.isAfter(expectedDt)) {
            throw new HarnessAssertionException(
                AssertionOperator.DATETIME_AFTER,
                path,
                expected,
                actual,
                "DATETIME_AFTER failed at " + path +
                    ". Expected after: " + expectedDt +
                    ", Actual: " + actualDt);
        }
    }
}
