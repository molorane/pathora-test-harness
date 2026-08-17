package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.engine.operator.OperatorAssertion;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class OperatorRegistry {

    private final Map<AssertionOperator, OperatorAssertion> operators;

    public OperatorRegistry() {
        this.operators = loadOperators();
    }

    private Map<AssertionOperator, OperatorAssertion> loadOperators() {
        return StreamSupport.stream(
                        ServiceLoader.load(OperatorAssertion.class).spliterator(),
                        false)
                .collect(Collectors.toUnmodifiableMap(
                        OperatorAssertion::operator,
                        Function.identity()
                ));
    }

    public OperatorAssertion get(AssertionOperator operator) {
        return operators.get(operator);
    }
}
