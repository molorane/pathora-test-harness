package za.co.nedbank.brm.testharness.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestTemplateLoader {

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
        return candidate;
    }
}