package za.co.pathora.testharness.util;

import za.co.pathora.testharness.model.RuleTestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FailureLogger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private FailureLogger() {
    }

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
        builder.append("ENTRY POINT : ").append(testCase.entryPointName()).append("\n");
        builder.append("TestName : ").append(testCase.testName()).append("\n");
        builder.append("TestDescription : ").append(testCase.testDescription()).append("\n\n");

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
