package io.github.molorane.pathora.testharness.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loader responsible for resolving and reading request template files from the file system.
 */
public class RequestTemplateLoader {

    /**
     * Resolves and loads the request template string.
     *
     * @param suitePath          the path of the test suite file referencing the template
     * @param defaultRequestPath the relative or absolute path of the request template
     * @return the string content of the request template
     * @throws IOException              if reading the file fails
     * @throws IllegalArgumentException if the file does not exist
     */
    public String loadTemplate(Path suitePath,
                               String defaultRequestPath) throws IOException {

        Path resolvedPath = resolvePath(suitePath, defaultRequestPath);

        if (Files.notExists(resolvedPath)) {
            throw new IllegalArgumentException(
                    "Request template not found: " + resolvedPath.toAbsolutePath()
            );
        }

        return Files.readString(resolvedPath);
    }

    private Path resolvePath(Path suitePath,
                             String requestPath) {

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

