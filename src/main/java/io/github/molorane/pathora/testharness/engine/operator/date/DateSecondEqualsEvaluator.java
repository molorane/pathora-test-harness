package io.github.molorane.pathora.testharness.engine.operator.date;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.DateUtils;

import java.time.LocalTime;

/**
 * Operator: {@code DATE_SECOND_EQUALS}
 *
 * <p>Validates that the second component (0-59) of the extracted time or datetime matches the expected second.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.timestamp",
 *   "Operator": "DATE_SECOND_EQUALS",
 *   "Value": 0
 * }
 * }</pre>
 *
 * @see AssertionOperator#DATE_SECOND_EQUALS
 */
public class DateSecondEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_SECOND_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalTime actualTime = DateUtils.parseTimeOrDateTime(normalizedActual, path);
        int expectedSecond = DateUtils.parseMinuteOrSecond(expected, "second", path + " (expected)");
        int actualSecond = actualTime.getSecond();

        if (actualSecond != expectedSecond) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_SECOND_EQUALS,
                    path,
                    expectedSecond,
                    actualSecond,
                    "DATE_SECOND_EQUALS failed at " + path +
                            ". Expected second: " + expectedSecond +
                            ", Actual second: " + actualSecond + " (from " + normalizedActual + ")");
        }
    }
}

