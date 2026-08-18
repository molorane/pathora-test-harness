package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class OperatorRegistry {

    private final Map<AssertionOperator, AssertionEvaluator> operators;

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

    public AssertionEvaluator get(AssertionOperator operator) {
        return operators.get(operator);
    }
}
