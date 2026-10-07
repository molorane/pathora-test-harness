package io.github.molorane.pathora.testharness.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OperatorRegistryTest {

    private static final Set<AssertionOperator> LOGICAL_OPERATORS = EnumSet.of(
            AssertionOperator.AND,
            AssertionOperator.OR,
            AssertionOperator.NOT
    );

    @Test
    @DisplayName("Verify every non-logical AssertionOperator has a registered AssertionEvaluator")
    void allNonLogicalOperatorsHaveEvaluator() {
        OperatorRegistry registry = new OperatorRegistry();

        for (AssertionOperator operator : AssertionOperator.values()) {
            if (LOGICAL_OPERATORS.contains(operator)) {
                continue;
            }

            AssertionEvaluator evaluator = registry.get(operator);
            assertThat(evaluator)
                    .as("Evaluator for operator " + operator + " should be registered")
                    .isNotNull();
            assertThat(evaluator.operator())
                    .as("Evaluator for " + operator + " must declare the matching operator")
                    .isEqualTo(operator);
        }
    }

    @Test
    @DisplayName("Verify all 138 AssertionOperators are defined and accounted for")
    void totalOperatorCount() {
        assertThat(AssertionOperator.values()).hasSize(138);
    }
}

