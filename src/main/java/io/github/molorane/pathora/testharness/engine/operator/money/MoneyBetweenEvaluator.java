package io.github.molorane.pathora.testharness.engine.operator.money;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;
import io.github.molorane.pathora.testharness.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Operator: {@code MONEY_BETWEEN}
 *
 * <p>Validates that the extracted monetary figure is between {@code min} and {@code max} bounds inclusive, verifying matching currencies if specified.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.loanAmount",
 *   "Operator": "MONEY_BETWEEN",
 *   "Value": {
 *     "min": "1000.00 ZAR",
 *     "max": "50000.00 ZAR"
 *   }
 * }
 * }</pre>
 *
 * @see AssertionOperator#MONEY_BETWEEN
 */
public class MoneyBetweenEvaluator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.MONEY_BETWEEN;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        Map<String, Object> range = AssertionUtils.toMap(expected);
        Object minObj = range.get("min");
        Object maxObj = range.get("max");

        if (minObj == null || maxObj == null) {
            throw new IllegalArgumentException(
                "MONEY_BETWEEN operator requires 'min' and 'max' in Value at " + path);
        }

        MoneyUtils.validateCurrencyMatch(path, normalizedActual, minObj, AssertionOperator.MONEY_BETWEEN);
        MoneyUtils.validateCurrencyMatch(path, normalizedActual, maxObj, AssertionOperator.MONEY_BETWEEN);

        BigDecimal actualAmount = MoneyUtils.requireAmount(path, normalizedActual);
        BigDecimal minAmount = MoneyUtils.requireAmount(path, minObj);
        BigDecimal maxAmount = MoneyUtils.requireAmount(path, maxObj);

        if (actualAmount.compareTo(minAmount) < 0 || actualAmount.compareTo(maxAmount) > 0) {
            throw new HarnessAssertionException(
                AssertionOperator.MONEY_BETWEEN,
                path,
                "between " + minAmount + " and " + maxAmount,
                actualAmount,
                "MONEY_BETWEEN failed at " + path +
                    ". Expected amount between " + minAmount + " and " + maxAmount +
                    ", Actual amount: " + actualAmount);
        }
    }
}
