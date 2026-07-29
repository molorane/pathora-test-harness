package com.example.demo.adapter;

import io.github.molorane.pathora.testharness.engine.EntryPointDispatcher;
import io.github.molorane.pathora.testharness.engine.JsonMutationEngine;
import io.github.molorane.pathora.testharness.engine.ResponseAssertionExecutor;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.loader.RequestLoader;
import io.github.molorane.pathora.testharness.loader.TestSuiteLoader;
import io.github.molorane.pathora.testharness.model.RuleTestCase;
import io.github.molorane.pathora.testharness.model.TestSuite;
import io.github.molorane.pathora.testharness.util.FailureLogger;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component
public class DynamicTestAdapter {

    private static final Logger log = LoggerFactory.getLogger(DynamicTestAdapter.class);

    private final TestSuiteLoader loader;
    private final RequestLoader templateLoader;
    private final JsonMutationEngine mutationEngine;
    private final EntryPointDispatcher dispatcher;
    private final ResponseAssertionExecutor assertionExecutor;

    private static final Path LOG_FILE = Paths.get("templates/report");

    public DynamicTestAdapter(
            TestSuiteLoader loader,
            RequestLoader templateLoader,
            JsonMutationEngine mutationEngine,
            EntryPointDispatcher dispatcher,
            ResponseAssertionExecutor assertionExecutor
    ) {
        this.loader = loader;
        this.templateLoader = templateLoader;
        this.mutationEngine = mutationEngine;
        this.dispatcher = dispatcher;
        this.assertionExecutor = assertionExecutor;
    }

    public Stream<DynamicNode> generate(String suitesDirectoryPath) {
        try {
            deleteDirectory(LOG_FILE);

            Path suitesPath = Paths.get(suitesDirectoryPath);
            AtomicInteger counter = new AtomicInteger(0);
            List<Path> suiteFiles = new ArrayList<>();

            if (Files.isDirectory(suitesPath)) {
                try (Stream<Path> stream = Files.list(suitesPath)) {
                    suiteFiles = stream
                            .filter(path -> path.toString().endsWith(".json"))
                            .sorted()
                            .toList();
                }
            } else if (Files.isRegularFile(suitesPath) && suitesPath.toString().endsWith(".json")) {
                suiteFiles = Collections.singletonList(suitesPath);
            } else {
                throw new IllegalArgumentException("Path must be a JSON file or directory: " + suitesDirectoryPath);
            }

            return suiteFiles.stream()
                    .map(suitePath -> {
                        try {
                            TestSuite suite = loader.load(suitePath);

                            Stream<DynamicTest> testCases =
                                    suite.tests().stream()
                                            .map(testCase ->
                                                    DynamicTest.dynamicTest(
                                                            counter.incrementAndGet()
                                                                    + " " + testCase.testName(),
                                                            () -> executeTestCase(
                                                                    suitePath,
                                                                    suite,
                                                                    testCase
                                                            )
                                                    )
                                            );

                            return DynamicContainer.dynamicContainer(
                                    suitePath.getFileName().toString(),
                                    testCases
                            );

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (Exception e) {
            throw new RuntimeException("Failed to load test suites", e);
        }
    }

    private void executeTestCase(
            Path suitePath,
            TestSuite suite,
            RuleTestCase testCase
    ) throws Exception {
        String testFileName = suitePath.getFileName().toString();

        // Arrange
        String mutatedRequest = arrangeMutatedRequest(suitePath, suite, testCase, testFileName);

        log.info("Executing test: {}", testCase.testName());
        log.info("Mutated Request: {}", mutatedRequest);

        // Act
        String response = dispatchToEngine(suite, testCase, mutatedRequest);

        // Assert
        assertAndReportFailure(testFileName, mutatedRequest, response, testCase);
    }

    private String arrangeMutatedRequest(
            Path suitePath,
            TestSuite suite,
            RuleTestCase testCase,
            String testFileName
    ) throws Exception {
        String baseRequest = templateLoader.loadTemplate(suitePath, suite.defaultRequestPath());

        return mutationEngine.apply(
                baseRequest,
                testCase.testCaseParameterValues(),
                testFileName,
                testCase.entryPointName(),
                suite.isXmlRequest()
        );
    }

    private String dispatchToEngine(TestSuite suite, RuleTestCase testCase, String mutatedRequest) throws Exception {
        return dispatcher.dispatch(testCase.entryPointName(), mutatedRequest, suite.isXmlRequest());
    }

    private void assertAndReportFailure(
            String testFileName,
            String mutatedRequest,
            String response,
            RuleTestCase testCase
    ) {
        Path reportFile = LOG_FILE.resolve(
                testFileName.replace(".json", "") + "__" + sanitizeForFileName(testCase.testName()) + ".json"
        );

        try {
            assertionExecutor.execute(mutatedRequest, response, testCase);
            log.info("Passed: {}", testCase.testName());
        } catch (HarnessAssertionException ex) {
            FailureLogger.logFailure(
                    testCase,
                    reportFile,
                    mutatedRequest,
                    response,
                    ex
            );

            log.error("Failed: {}", testCase.testName());

            throw ex;
        } catch (Exception ex) {
            FailureLogger.logFailure(
                    testCase,
                    reportFile,
                    mutatedRequest,
                    response,
                    new AssertionError(
                            "SYSTEM_FAILURE\n" + ex.getMessage(), ex
                    )
            );

            log.error("System Failure: {}", testCase.testName());

            throw ex;
        }
    }

    private static String sanitizeForFileName(String value) {
        return value.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    private static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException ignored) {
                            }
                        });
            }
        }
    }
}
