package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;

public class DateBeforeNowOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = DurationHelper.parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime now = LocalDateTime.now();

        if (!actualDt.isBefore(now)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_BEFORE_NOW,
                    path,
                    "before " + now,
                    actualDt,
                    "DATE_BEFORE_NOW failed at " + path +
                            ". Value " + actualDt + " is not before now (" + now + ")");
        }
    }
}
