package io.github.molorane.pathora.testharness.util;

import io.github.molorane.pathora.testharness.model.RuleTestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility for recording assertion and mutation failure reports to disk.
 */
public final class FailureLogger {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private FailureLogger() {
    }

    /**
     * Appends a detailed failure block to the specified log file.
     *
     * @param testCase       the failing rule test case
     * @param testFileName   the destination log file path
     * @param mutatedRequest the mutated request JSON or XML string sent to the entry point
     * @param response       the response object or string received
     * @param error          the assertion error that caused the failure
     */
    public static synchronized void logFailure(
        RuleTestCase testCase,
        Path testFileName,
        String mutatedRequest,
        Object response,
        AssertionError error
    ) {

        String timestamp = LocalDateTime.now().format(FORMATTER);

        StringBuilder builder = new StringBuilder();

        builder.append("\n====================================================\n");
        builder.append("FAILURE TIME: ").append(timestamp).append("\n");
        builder.append("JSON FILE : ").append(testFileName.getFileName().toString()).append("\n");
        builder.append("OPERATION : ").append(testCase.operation()).append("\n");
        builder.append("NAME : ").append(testCase.name()).append("\n");
        builder.append("DESCRIPTION : ").append(testCase.description()).append("\n\n");

        builder.append("MUTATED REQUEST:\n");
        builder.append(pretty(mutatedRequest)).append("\n\n");

        builder.append("RESPONSE:\n");
        builder.append(pretty(response)).append("\n\n");

        builder.append("ERROR:");
        builder.append(error.getMessage()).append("\n");
        builder.append("====================================================\n");

        try {
            Files.createDirectories(testFileName.getParent());
            Files.writeString(
                testFileName,
                builder.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to write failure log", e);
        }
    }

    private static String pretty(Object value) {
        if (value == null) return "null";
        return value.toString();
    }
}


