package com.example.demo;

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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Demonstrates testing test suite files individually (one file at a time).
 */
@SpringBootTest
class SingleTestSuiteDemoTest {

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
    @DisplayName("Verify Spring Boot Context Loads Executors")
    void contextLoads() {
        assertThat(executorBeans).hasSize(6);
    }

    @Test
    @DisplayName("Execute User Registration Test Suite")
    void testUserRegistrationSuite() throws Exception {
        runTestSuite("templates/tests/user-create-test.json");
    }

    @Test
    @DisplayName("Execute Order Checkout Test Suite")
    void testOrderCheckoutSuite() throws Exception {
        runTestSuite("templates/tests/order-checkout-test.json");
    }

    @Test
    @DisplayName("Execute Payment Gateway Test Suite")
    void testPaymentGatewaySuite() throws Exception {
        runTestSuite("templates/tests/payment-process-test.json");
    }

    @Test
    @DisplayName("Execute Inventory Update Test Suite")
    void testInventoryUpdateSuite() throws Exception {
        runTestSuite("templates/tests/inventory-update-test.json");
    }

    @Test
    @DisplayName("Execute Loan Application Test Suite")
    void testLoanApplicationSuite() throws Exception {
        runTestSuite("templates/tests/loan-application-test.json");
    }

    @Test
    @DisplayName("Execute Deeply Nested Policy Evaluation Test Suite")
    void testPolicyEvaluationSuite() throws Exception {
        runTestSuite("templates/tests/policy-evaluation-test.json");
    }

    @Test
    @DisplayName("Execute Deeply Nested Policy Risk Assessment Test Suite")
    void testPolicyRiskAssessmentSuite() throws Exception {
        runTestSuite("templates/tests/policy-risk-assessment-test.json");
    }

    private void runTestSuite(String testSuiteRelativePath) throws Exception {
        Path suitePath = Paths.get(testSuiteRelativePath);
        assertThat(Files.exists(suitePath))
                .withFailMessage("Test suite file not found: " + suitePath.toAbsolutePath())
                .isTrue();

        TestSuite suite = testSuiteLoader.load(suitePath);
        assertThat(suite).isNotNull();

        Path requestPath = suitePath.getParent().resolve(suite.defaultJSONRequestPath()).normalize();
        assertThat(Files.exists(requestPath))
                .withFailMessage("Request template file not found: " + requestPath.toAbsolutePath())
                .isTrue();

        String rawRequestJson = Files.readString(requestPath);

        for (RuleTestCase testCase : suite.tests()) {
            assertDoesNotThrow(() -> {
                String mutatedRequest = mutationEngine.apply(
                        rawRequestJson,
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
