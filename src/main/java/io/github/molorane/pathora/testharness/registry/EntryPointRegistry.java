package io.github.molorane.pathora.testharness.registry;

import io.github.molorane.pathora.testharness.spi.EntryPointExecutor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry holding all available {@link EntryPointExecutor} instances indexed by their entry point name.
 */
public class EntryPointRegistry {

    private final Map<String, EntryPointExecutor<?, ?>> executors;

    /**
     * Constructs an {@code EntryPointRegistry} initialized with a list of executors.
     *
     * @param executors the list of entry point executors to register
     */
    public EntryPointRegistry(List<EntryPointExecutor<?, ?>> executors) {
        this.executors = executors.stream()
                .collect(Collectors.toMap(
                        EntryPointExecutor::getEntryPointName,
                        Function.identity()));
    }

    /**
     * Retrieves the executor registered for the specified entry point name.
     *
     * @param name the entry point name to look up
     * @return the corresponding {@link EntryPointExecutor}
     * @throws IllegalArgumentException if no executor is registered under the given name
     */
    public EntryPointExecutor<?, ?> get(String name) {
        return Optional.ofNullable(executors.get(name))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No executor for entry point: " + name));
    }
}



