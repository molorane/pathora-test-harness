package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("Allow registration of custom operator names via plugin registry")
    void customOperatorCanBeRegistered() {
        OperatorRegistry registry = new OperatorRegistry();
        registry.register("HAS_ACTIVE_SUBSCRIPTION", new AssertionEvaluator() {
            @Override
            public AssertionOperator operator() {
                return AssertionOperator.EQUALS;
            }

            @Override
            public String operatorName() {
                return "HAS_ACTIVE_SUBSCRIPTION";
            }

            @Override
            public void apply(String path, Object actual, Object expected, boolean pathExists) {
                // no-op for registration test
            }
        });

        assertThat(registry.get("HAS_ACTIVE_SUBSCRIPTION")).isNotNull();
        assertThat(registry.get("has_active_subscription")).isNotNull();
    }
}
