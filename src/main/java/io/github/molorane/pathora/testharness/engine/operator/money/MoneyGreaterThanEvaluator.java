package io.github.molorane.pathora.testharness.engine.operator.money;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.MoneyUtils;

import java.math.BigDecimal;

/**
 * Operator: {@code MONEY_GREATER_THAN}
 *
 * <p>Validates that the extracted monetary figure is strictly greater than expected threshold.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.salary",
 *   "Operator": "MONEY_GREATER_THAN",
 *   "Value": "15000.00 ZAR"
 * }
 * }</pre>
 *
 * @see AssertionOperator#MONEY_GREATER_THAN
 */
public class MoneyGreaterThanEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.MONEY_GREATER_THAN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        MoneyUtils.validateCurrencyMatch(path, normalizedActual, expected, AssertionOperator.MONEY_GREATER_THAN);

        BigDecimal actualAmount = MoneyUtils.requireAmount(path, normalizedActual);
        BigDecimal expectedAmount = MoneyUtils.requireAmount(path, expected);

        if (actualAmount.compareTo(expectedAmount) <= 0) {
            throw new HarnessAssertionException(
                    AssertionOperator.MONEY_GREATER_THAN,
                    path,
                    expected,
                    actual,
                    "MONEY_GREATER_THAN failed at " + path +
                            ". Expected amount > " + expectedAmount +
                            ", Actual amount: " + actualAmount);
        }
    }
}
