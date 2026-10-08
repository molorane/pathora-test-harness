package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Registry that discovers and maintains all available {@link AssertionEvaluator} service implementations.
 */
public final class OperatorRegistry {

    private final Map<AssertionOperator, AssertionEvaluator> operators;
    private final Map<String, AssertionEvaluator> customOperators;
    private final Map<String, AssertionEvaluator> operatorsByName;

    /**
     * Constructs the registry by loading all {@link AssertionEvaluator} service implementations.
     */
    public OperatorRegistry() {
        this.operators = loadOperators();
        this.operatorsByName = indexOperatorsByName(operators);
        this.customOperators = new LinkedHashMap<>();
    }

    private Map<AssertionOperator, AssertionEvaluator> loadOperators() {
        return StreamSupport.stream(
                ServiceLoader.load(AssertionEvaluator.class).spliterator(),
                false)
            .collect(Collectors.toUnmodifiableMap(
                evaluator -> {
                    try {
                        return evaluator.operator();
                    } catch (UnsupportedOperationException e) {
                        throw new IllegalStateException(
                            "Built-in providers must implement operator(); custom plugins must register via register(String, AssertionEvaluator)",
                            e);
                    }
                },
                Function.identity(),
                (left, right) -> {
                    throw new IllegalStateException(
                        "Duplicate operator registration for: " + left.operatorName());
                }
            ));
    }

    private Map<String, AssertionEvaluator> indexOperatorsByName(Map<AssertionOperator, AssertionEvaluator> operators) {
        Map<String, AssertionEvaluator> indexed = new LinkedHashMap<>();
        for (Map.Entry<AssertionOperator, AssertionEvaluator> entry : operators.entrySet()) {
            String key = normalize(entry.getValue().operatorName());
            AssertionEvaluator previous = indexed.putIfAbsent(key, entry.getValue());
            if (previous != null) {
                throw new IllegalStateException(
                    "Duplicate operator registration for: " + key);
            }
        }
        return Collections.unmodifiableMap(indexed);
    }

    /**
     * Registers a custom operator by name.
     *
     * @param name      the custom operator name, case-insensitive
     * @param evaluator the evaluator implementation
     */
    public void register(String name, AssertionEvaluator evaluator) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Operator name must not be blank");
        }
        if (evaluator == null) {
            throw new IllegalArgumentException("Evaluator must not be null");
        }

        String normalized = normalize(name);
        if (operatorsByName.containsKey(normalized) && operatorsByName.get(normalized) != evaluator) {
            throw new IllegalStateException(
                "Operator name already registered as a built-in operator: " + name);
        }
        customOperators.put(normalized, evaluator);
    }

    /**
     * Resolves a built-in or custom operator by enum or string name.
     *
     * @param operator the assertion operator
     * @return the registered {@link AssertionEvaluator}, or {@code null} if not found
     */
    public AssertionEvaluator get(AssertionOperator operator) {
        return operators.get(operator);
    }

    /**
     * Resolves a built-in or custom operator by string name.
     *
     * @param name the operator name
     * @return the registered {@link AssertionEvaluator}, or {@code null} if not found
     */
    public AssertionEvaluator get(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String normalized = normalize(name);
        AssertionEvaluator custom = customOperators.get(normalized);
        if (custom != null) {
            return custom;
        }
        return operatorsByName.get(normalized);
    }

    /**
     * Returns an immutable view of all registered custom operators.
     *
     * @return a map of custom operator names to evaluators
     */
    public Map<String, AssertionEvaluator> customOperators() {
        return Collections.unmodifiableMap(customOperators);
    }

    private String normalize(String name) {
        return name.trim().toUpperCase();
    }
}
