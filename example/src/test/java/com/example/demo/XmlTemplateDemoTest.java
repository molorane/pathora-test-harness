package com.example.demo;

import com.example.demo.config.TestHarnessConfig;
import io.github.molorane.pathora.testharness.engine.AssertionEngine;
import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.loader.TestSuiteLoader;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;
import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Dedicated test class demonstrating testing using XML request templates (.xml).
 */
@SpringBootTest
@Import(TestHarnessConfig.class)
class XmlTemplateDemoTest {

    @Autowired
    private List<EntryPointExecutor> executorBeans;

    @Autowired
    private EntryPointDispatcher dispatcher;

    @Autowired
    private TestSuiteLoader testSuiteLoader;

    @Autowired
    private JsonMutationEngine mutationEngine;

    @Autowired
    private AssertionEngine assertionEngine;

    @Test
    @DisplayName("Verify Spring Boot Context Loads Executors for XML Tests")
    void contextLoads() {
        assertThat(executorBeans).isNotEmpty();
    }

    @Test
    @DisplayName("Execute User Registration Test Suite using XML Request Template")
    void testUserRegistrationXmlSuite() throws Exception {
        runTestSuite("templates/tests/user-create-xml-test.json");
    }

    @Test
    @DisplayName("Execute Deeply Nested Policy Evaluation Test Suite using XML Request Template")
    void testComplexPolicyXmlSuite() throws Exception {
        runTestSuite("templates/tests/policy-evaluation-xml-test.json");
    }

    private void runTestSuite(String testSuiteRelativePath) throws Exception {
        Path suitePath = Paths.get(testSuiteRelativePath);
        assertThat(Files.exists(suitePath))
                .withFailMessage("Test suite file not found: " + suitePath.toAbsolutePath())
                .isTrue();

        TestSuite suite = testSuiteLoader.load(suitePath);
        assertThat(suite).isNotNull();

        String defaultRequestPath = suite.defaultRequestPath();
        Path requestPath = suitePath.getParent().resolve(defaultRequestPath).normalize();
        assertThat(Files.exists(requestPath))
                .withFailMessage("Request template file not found: " + requestPath.toAbsolutePath())
                .isTrue();

        String rawRequest = Files.readString(requestPath);

        for (RuleTestCase testCase : suite.tests()) {
            assertDoesNotThrow(() -> {
                String mutatedRequest = mutationEngine.apply(
                        rawRequest,
                        testCase.testCaseParameterValues(),
                        suitePath.getFileName().toString(),
                        testCase.entryPointName()
                );

                String responseJson = dispatcher.dispatch(testCase.entryPointName(), mutatedRequest);
                assertThat(responseJson).isNotNull().isNotEmpty();

                assertionEngine.assertResponse(responseJson, testCase, mutatedRequest);
            }, "Test case '" + testCase.testName() + "' failed execution or assertions");
        }
    }
}
