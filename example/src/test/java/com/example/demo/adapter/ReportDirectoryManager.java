package com.example.demo.adapter;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

@Component
class ReportDirectoryManager {

    private static final Path LOG_FILE = Paths.get("templates/report");

    Path prepareReportDirectory() {
        try {
            deleteDirectory(LOG_FILE);
            Files.createDirectories(LOG_FILE);
            return LOG_FILE;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to prepare report directory: " + LOG_FILE.toAbsolutePath(), ex);
        }
    }

    private static void deleteDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

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

