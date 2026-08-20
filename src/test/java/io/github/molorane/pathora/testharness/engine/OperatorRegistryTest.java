package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OperatorRegistryTest {

    private OperatorRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new OperatorRegistry();
    }

    @Test
    @DisplayName("PASS: get returns registered evaluator for EQUALS operator")
    void shouldReturnEvaluatorForEquals() {
        AssertionEvaluator evaluator = registry.get(AssertionOperator.EQUALS);

        assertThat(evaluator).isNotNull();
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.EQUALS);
    }

    @Test
    @DisplayName("PASS: get returns registered evaluator for MONEY_EQUALS operator")
    void shouldReturnEvaluatorForMoneyEquals() {
        AssertionEvaluator evaluator = registry.get(AssertionOperator.MONEY_EQUALS);

        assertThat(evaluator).isNotNull();
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.MONEY_EQUALS);
    }

    @Test
    @DisplayName("PASS: get returns registered evaluator for MONEY_EQUALS_WITH_TOLERANCE operator")
    void shouldReturnEvaluatorForMoneyEqualsWithTolerance() {
        AssertionEvaluator evaluator = registry.get(AssertionOperator.MONEY_EQUALS_WITH_TOLERANCE);

        assertThat(evaluator).isNotNull();
        assertThat(evaluator.operator()).isEqualTo(AssertionOperator.MONEY_EQUALS_WITH_TOLERANCE);
    }

    @Test
    @DisplayName("PASS: verify all non-logical operators in AssertionOperator have registered evaluators")
    void shouldHaveEvaluatorsForAllNonLogicalOperators() {
        for (AssertionOperator op : AssertionOperator.values()) {
            if (op == AssertionOperator.AND || op == AssertionOperator.OR || op == AssertionOperator.NOT) {
                continue;
            }
            AssertionEvaluator evaluator = registry.get(op);
            assertThat(evaluator)
                    .withFailMessage("Missing registered evaluator for AssertionOperator: %s", op)
                    .isNotNull();
            assertThat(evaluator.operator()).isEqualTo(op);
        }
    }

    @Test
    @DisplayName("PASS: get returns null when operator is null")
    void shouldReturnNullWhenOperatorIsNull() {
        assertThat(registry.get(null)).isNull();
    }
}
