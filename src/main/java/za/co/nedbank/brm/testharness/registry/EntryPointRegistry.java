package za.co.nedbank.brm.testharness.registry;

import za.co.nedbank.brm.testharness.spi.EntryPointExecutor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntryPointRegistry {

    private final Map<String, EntryPointExecutor> executors;

    public EntryPointRegistry(List<EntryPointExecutor> executors) {
        this.executors = executors.stream()
                .collect(Collectors.toMap(
                        EntryPointExecutor::getEntryPointName,
                        Function.identity()));
    }

    public EntryPointExecutor get(String name) {
        return Optional.ofNullable(executors.get(name))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No executor for entry point: " + name));
    }
}