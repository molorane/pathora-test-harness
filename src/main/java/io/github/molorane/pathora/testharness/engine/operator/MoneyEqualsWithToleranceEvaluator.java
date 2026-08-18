package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.Map;

public class MoneyEqualsWithToleranceEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.MONEY_EQUALS_WITH_TOLERANCE;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        Map<String, Object> config = AssertionUtils.toMap(expected);
        Object expObj = config.get("expected");
        if (expObj == null) {
            expObj = config.get("value");
        }
        Object tolObj = config.get("tolerance");

        if (expObj == null || tolObj == null) {
            throw new IllegalArgumentException(
                    "MONEY_EQUALS_WITH_TOLERANCE requires 'expected' (or 'value') and 'tolerance' in Value at " + path);
        }

        MoneyUtils.validateCurrencyMatch(path, normalizedActual, expected, AssertionOperator.MONEY_EQUALS_WITH_TOLERANCE);

        BigDecimal actualAmount = MoneyUtils.requireAmount(path, normalizedActual);
        BigDecimal expectedAmount = MoneyUtils.requireAmount(path, expObj);
        BigDecimal tolerance = MoneyUtils.requireAmount(path, tolObj);

        if (!MoneyUtils.isWithinTolerance(actualAmount, expectedAmount, tolerance)) {
            throw new HarnessAssertionException(
                    AssertionOperator.MONEY_EQUALS_WITH_TOLERANCE,
                    path,
                    expectedAmount + " (±" + tolerance + ")",
                    actualAmount,
                    "MONEY_EQUALS_WITH_TOLERANCE failed at " + path +
                            ". Expected: " + expectedAmount + " ±" + tolerance +
                            ", Actual: " + actualAmount);
        }
    }
}
