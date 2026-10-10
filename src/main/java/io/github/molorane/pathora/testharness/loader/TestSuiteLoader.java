package io.github.molorane.pathora.testharness.loader;

import io.github.molorane.pathora.testharness.model.TestSuite;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads and parses {@link TestSuite} configuration files from the filesystem.
 */
public class TestSuiteLoader {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new {@code TestSuiteLoader} with the specified {@link ObjectMapper}.
     *
     * @param objectMapper the object mapper used for JSON deserialization
     */
    public TestSuiteLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Loads a test suite definition from the specified file path.
     *
     * @param suitePath the path to the test suite file
     * @return the deserialized {@link TestSuite} instance
     * @throws IOException if an I/O error occurs while reading the file
     */
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