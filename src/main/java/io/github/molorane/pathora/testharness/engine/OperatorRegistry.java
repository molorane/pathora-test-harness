package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Registry that discovers and maintains all available {@link AssertionEvaluator} service implementations.
 */
public final class OperatorRegistry {

    private final Map<AssertionOperator, AssertionEvaluator> operators;

    /**
     * Constructs the registry by loading all {@link AssertionEvaluator} services via {@link ServiceLoader}.
     */
    public OperatorRegistry() {
        this.operators = loadOperators();
    }

    private Map<AssertionOperator, AssertionEvaluator> loadOperators() {
        return StreamSupport.stream(
                ServiceLoader.load(AssertionEvaluator.class).spliterator(),
                false)
            .collect(Collectors.toUnmodifiableMap(
                AssertionEvaluator::operator,
                Function.identity()
            ));
    }

    /**
     * Retrieves the evaluator associated with the specified operator.
     *
     * @param operator the assertion operator
     * @return the registered {@link AssertionEvaluator}, or {@code null} if not found
     */
    public AssertionEvaluator get(AssertionOperator operator) {
        return operators.get(operator);
    }
}


