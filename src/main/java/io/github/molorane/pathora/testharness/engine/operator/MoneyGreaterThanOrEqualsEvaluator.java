package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.MoneyUtils;

import java.math.BigDecimal;

public class MoneyGreaterThanOrEqualsEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.MONEY_GREATER_THAN_OR_EQUALS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        MoneyUtils.validateCurrencyMatch(path, normalizedActual, expected, AssertionOperator.MONEY_GREATER_THAN_OR_EQUALS);

        BigDecimal actualAmount = MoneyUtils.requireAmount(path, normalizedActual);
        BigDecimal expectedAmount = MoneyUtils.requireAmount(path, expected);

        if (actualAmount.compareTo(expectedAmount) < 0) {
            throw new HarnessAssertionException(
                    AssertionOperator.MONEY_GREATER_THAN_OR_EQUALS,
                    path,
                    expected,
                    actual,
                    "MONEY_GREATER_THAN_OR_EQUALS failed at " + path +
                            ". Expected amount >= " + expectedAmount +
                            ", Actual amount: " + actualAmount);
        }
    }
}
