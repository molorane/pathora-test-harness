package com.example.demo.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.loader.RequestLoader;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.nio.file.Path;
import java.time.Clock;
import java.time.ZoneId;

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
        String effectiveTimezone = (testCase.timezone() != null && !testCase.timezone().isBlank())
                ? testCase.timezone()
                : suite.timezone();
        boolean customTimezone = effectiveTimezone != null && !effectiveTimezone.isBlank();

        RuleTestCase effectiveTestCase = customTimezone && (testCase.timezone() == null || testCase.timezone().isBlank())
                ? new RuleTestCase(testCase.name(), testCase.description(), testCase.operation(), testCase.mutations(), testCase.assertions(), effectiveTimezone)
                : testCase;

        if (customTimezone) {
            PathoraClock.setThreadClock(Clock.system(ZoneId.of(effectiveTimezone.trim())));
        }

        try {
            String testFileName = suitePath.getFileName().toString();
            String mutatedRequest = arrangeMutatedRequest(suitePath, suite, effectiveTestCase, testFileName);

            log.info("Executing test: {}", effectiveTestCase.name());
            log.info("Mutated Request: {}", mutatedRequest);

            String response = dispatchToEngine(suite, effectiveTestCase, mutatedRequest);
            assertionFailureReporter.assertAndReport(testFileName, mutatedRequest, response, reportDirectory, effectiveTestCase);
        } finally {
            if (customTimezone) {
                PathoraClock.clearThreadClock();
            }
        }
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

