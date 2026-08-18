package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.model.AssertionOperator;

public class ExistsOperator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.EXISTS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        if (!pathExists) {
            throw new AssertionError(
                    "Expected path to exist: " + path);
        }
    }
}
