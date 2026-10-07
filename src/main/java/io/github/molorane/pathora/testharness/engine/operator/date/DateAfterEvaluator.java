package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDate;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDate;

/**
 * Operator: {@code DATE_AFTER}
 *
 * <p>Validates that the extracted date is chronologically after the expected date.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.expiryDate",
 *   "Operator": "DATE_AFTER",
 *   "Value": "2025-01-01"
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_AFTER
 */
public class DateAfterEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_AFTER;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);
        LocalDate expectedDate = parseDate(String.valueOf(expected), path);

        if (!actualDate.isAfter(expectedDate)) {
            throw new HarnessAssertionException(
                AssertionOperator.DATE_AFTER,
                path,
                expected,
                actual,
                "DATE_AFTER failed at " + path +
                    ". Expected after: " + expectedDate +
                    ", Actual: " + actualDate);
        }
    }
}
