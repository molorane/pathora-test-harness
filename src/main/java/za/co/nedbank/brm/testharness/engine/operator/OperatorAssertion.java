package za.co.nedbank.brm.testharness.engine.operator;

public interface OperatorAssertion {

    void apply(String path, Object actual, Object expected, boolean pathExists);
}
