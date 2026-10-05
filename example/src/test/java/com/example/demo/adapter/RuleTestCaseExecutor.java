package com.example.demo.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.loader.RequestLoader;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;

import java.nio.file.Path;

@Component
class RuleTestCaseExecutor {

    private static final Logger log = LoggerFactory.getLogger(RuleTestCaseExecutor.class);

    private final RequestLoader templateLoader;
    private final JsonMutationEngine mutationEngine;
    private final EntryPointDispatcher dispatcher;
    private final AssertionFailureReporter assertionFailureReporter;

    RuleTestCaseExecutor(
            RequestLoader templateLoader,
            JsonMutationEngine mutationEngine,
            EntryPointDispatcher dispatcher,
            AssertionFailureReporter assertionFailureReporter
    ) {
        this.templateLoader = templateLoader;
        this.mutationEngine = mutationEngine;
        this.dispatcher = dispatcher;
        this.assertionFailureReporter = assertionFailureReporter;
    }

    void execute(Path suitePath, TestSuite suite, RuleTestCase testCase, Path reportDirectory) {
        String testFileName = suitePath.getFileName().toString();
        String mutatedRequest = arrangeMutatedRequest(suitePath, suite, testCase, testFileName);

        log.info("Executing test: {}", testCase.name());
        log.info("Mutated Request: {}", mutatedRequest);

        String response = dispatchToEngine(suite, testCase, mutatedRequest);
        assertionFailureReporter.assertAndReport(testFileName, mutatedRequest, response, reportDirectory, testCase);
    }

    private String arrangeMutatedRequest(
            Path suitePath,
            TestSuite suite,
            RuleTestCase testCase,
            String testFileName
    ) {
        try {
            String baseRequest = templateLoader.loadTemplate(suitePath, suite.defaultRequestPath());

            return mutationEngine.apply(
                    baseRequest,
                    testCase.mutations(),
                    testFileName,
                    testCase.operation(),
                    suite.isXmlRequest()
            );
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to prepare request for test '" + testCase.name() + "' in suite '" + testFileName + "'",
                    ex
            );
        }
    }

    private String dispatchToEngine(TestSuite suite, RuleTestCase testCase, String mutatedRequest) {
        try {
            return dispatcher.dispatch(testCase.operation(), mutatedRequest, suite.isXmlRequest());
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to dispatch request for operation: " + testCase.operation(),
                    ex
            );
        }
    }
}

