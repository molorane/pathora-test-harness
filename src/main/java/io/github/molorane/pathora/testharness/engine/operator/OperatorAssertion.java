package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.model.AssertionOperator;

public interface OperatorAssertion {

    AssertionOperator operator();
    void apply(String path, Object actual, Object expected, boolean pathExists);
}
