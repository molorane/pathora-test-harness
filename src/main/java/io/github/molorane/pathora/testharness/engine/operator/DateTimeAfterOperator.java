package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;

public class DateTimeAfterOperator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATETIME_AFTER;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = DateTimeBeforeOperator.parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime expectedDt = DateTimeBeforeOperator.parseDateTime(String.valueOf(expected), path);

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
