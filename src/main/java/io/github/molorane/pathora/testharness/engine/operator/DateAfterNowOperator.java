package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.time.LocalDateTime;

public class DateAfterNowOperator implements OperatorAssertion {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.DATE_AFTER_NOW;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);
        LocalDateTime actualDt = DurationHelper.parseDateTime(String.valueOf(normalizedActual), path);
        LocalDateTime now = LocalDateTime.now();

        if (!actualDt.isAfter(now)) {
            throw new HarnessAssertionException(
                    AssertionOperator.DATE_AFTER_NOW,
                    path,
                    "after " + now,
                    actualDt,
                    "DATE_AFTER_NOW failed at " + path +
                            ". Value " + actualDt + " is not after now (" + now + ")");
        }
    }
}
