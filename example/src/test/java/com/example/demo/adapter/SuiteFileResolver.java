package com.example.demo.adapter;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
class SuiteFileResolver {

    List<Path> resolve(String suitesDirectoryPath) {
        Path suitesPath = Paths.get(suitesDirectoryPath);

        if (Files.isDirectory(suitesPath)) {
            try (Stream<Path> stream = Files.list(suitesPath)) {
                return stream
                        .filter(path -> path.toString().endsWith(".json"))
                        .sorted()
                        .toList();
            } catch (IOException ex) {
                throw new IllegalStateException("Failed to list suite directory: " + suitesPath, ex);
            }
        }

        if (Files.isRegularFile(suitesPath) && suitesPath.toString().endsWith(".json")) {
            return Collections.singletonList(suitesPath);
        }

        throw new IllegalArgumentException("Path must be a JSON file or directory: " + suitesDirectoryPath);
    }
}


