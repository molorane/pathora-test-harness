package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDate;

import static io.github.molorane.pathora.testharness.util.DateUtils.parseDate;

/**
 * Operator: {@code DATE_BEFORE}
 *
 * <p>Validates that the extracted date is chronologically before the expected date.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.creationDate",
 *   "Operator": "DATE_BEFORE",
 *   "Value": "2026-01-01"
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_BEFORE
 */
public class DateBeforeEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_BEFORE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDate actualDate = parseDate(String.valueOf(normalizedActual), path);
        LocalDate expectedDate = parseDate(String.valueOf(expected), path);

        if (!actualDate.isBefore(expectedDate)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_BEFORE,
                    path,
                    expected,
                    actual,
                    "DATE_BEFORE failed at " + path +
                            ". Expected before: " + expectedDate +
                            ", Actual: " + actualDate);
        }
    }
}
