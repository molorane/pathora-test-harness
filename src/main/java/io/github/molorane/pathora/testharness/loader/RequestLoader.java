package io.github.molorane.pathora.testharness.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility for loading request payload templates from the file system.
 */
public class RequestLoader {

    /**
     * Loads the raw template string from the given request path, resolving relative paths against the suite file directory.
     *
     * @param suitePath          the path to the test suite definition file
     * @param defaultRequestPath the configured request template path (absolute or relative)
     * @return the content of the request template file as a string
     * @throws IOException              if an I/O error occurs reading the file
     * @throws IllegalArgumentException if the resolved request template file does not exist
     */
    public String loadTemplate(Path suitePath, String defaultRequestPath) throws IOException {

        Path resolvedPath = resolvePath(suitePath, defaultRequestPath);

        if (Files.notExists(resolvedPath)) {
            throw new IllegalArgumentException(
                "Request template not found: " + resolvedPath.toAbsolutePath()
            );
        }

        return Files.readString(resolvedPath);
    }

    private Path resolvePath(Path suitePath, String requestPath) {

        Path candidate = Path.of(requestPath);

        // Absolute path
        if (candidate.isAbsolute()) {
            return candidate;
        }

        // Relative to suite file directory
        if (suitePath != null && suitePath.getParent() != null) {
            return suitePath.getParent().resolve(candidate).normalize();
        }

        return candidate;
    }
}

