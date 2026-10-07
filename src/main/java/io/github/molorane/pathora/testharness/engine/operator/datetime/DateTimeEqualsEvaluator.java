package io.github.molorane.pathora.testharness.engine.operator.datetime;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDateTime;

/**
 * Operator: {@code DATETIME_EQUALS}
 *
 * <p>Validates that the extracted datetime timestamp is strictly equal to expected datetime.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.publishedAt",
 *   "Operator": "DATETIME_EQUALS",
 *   "Value": "2025-06-15T12:00:00"
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATETIME_EQUALS
 */
public class DateTimeEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATETIME_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDate = parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime expectedDate = parseDateTime(String.valueOf(expected), path + " (expected)");

        if (!actualDate.isEqual(expectedDate)) {
            throw new HarnessAssertionException(
                AssertionOperator.DATETIME_EQUALS,
                path,
                expectedDate,
                actualDate,
                "DATETIME_EQUALS failed at " + path +
                    ". Expected datetime: " + expectedDate +
                    ", Actual: " + actualDate);
        }
    }
}

