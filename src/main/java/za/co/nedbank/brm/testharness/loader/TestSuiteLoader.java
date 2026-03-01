package za.co.nedbank.brm.testharness.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.nedbank.brm.testharness.model.TestSuite;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestSuiteLoader {

    private final ObjectMapper objectMapper;

    public TestSuiteLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TestSuite load(Path suitePath) throws IOException {

        if (Files.notExists(suitePath)) {
            throw new IllegalArgumentException(
                "Test suite file does not exist: " + suitePath.toAbsolutePath()
            );
        }

        try (InputStream inputStream = Files.newInputStream(suitePath)) {
            return objectMapper.readValue(inputStream, TestSuite.class);
        }
    }
}