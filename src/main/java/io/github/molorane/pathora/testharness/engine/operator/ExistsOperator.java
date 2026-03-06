package io.github.molorane.pathora.testharness.engine.operator;

public class ExistsOperator implements OperatorAssertion {

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        if (!pathExists) {
            throw new AssertionError(
                    "Expected path to exist: " + path);
        }
    }
}
