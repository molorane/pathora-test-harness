package io.github.molorane.pathora.testharness.engine.integration;

import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.model.JsonAssertion;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import org.junit.jupiter.api.BeforeEach;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class BaseIntegrationTest {

    protected AssertionEngine engine;

    @BeforeEach
    void setUpEngine() {
        engine = new AssertionEngine();
    }

    protected String loadJson(String filename) {
        String path = "/integration/" + filename;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource file not found: " + path);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON resource file: " + path, e);
        }
    }

    protected JsonAssertion assertion(String jsonPath, AssertionOperator operator, Object value) {
        return new JsonAssertion(jsonPath, operator, value, null, null);
    }

    protected RuleTestCase testCase(String name, List<JsonAssertion> assertions) {
        return new RuleTestCase(name, "Categorized Integration Test", "EntryPoint", List.of(), assertions);
    }
}
