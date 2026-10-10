package io.github.molorane.pathora.testharness.engine.expression;

import io.github.molorane.pathora.testharness.engine.expression.date.DateTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.datetime.DateTimeTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.epoch.EpochMillisTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.epoch.EpochSecondsTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomAlphanumericTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomBooleanTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomEmailTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomIntTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.uuid.UuidTokenEvaluator;

import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry for discovering, registering, and resolving dynamic {@link ExpressionTokenEvaluator} strategy implementations.
 *
 * <p>Supports built-in evaluators, runtime programmatically registered evaluators, and dynamic discovery
 * via Java {@link ServiceLoader}.</p>
 */
public final class ExpressionRegistry {

    private static final Map<String, ExpressionTokenEvaluator> REGISTRY = new ConcurrentHashMap<>();

    static {
        // Register default built-in date, time, and timestamp evaluators
        register(new DateTokenEvaluator());
        register(new DateTimeTokenEvaluator());
        register(new EpochMillisTokenEvaluator());
        register(new EpochSecondsTokenEvaluator());

        // Register default built-in random & identifier evaluators
        register(new UuidTokenEvaluator());
        register(new RandomIntTokenEvaluator());
        register(new RandomAlphanumericTokenEvaluator());
        register(new RandomEmailTokenEvaluator());
        register(new RandomBooleanTokenEvaluator());

        // Load custom plugin evaluators via ServiceLoader
        try {
            ServiceLoader.load(ExpressionTokenEvaluator.class).forEach(ExpressionRegistry::register);
        } catch (Exception ignored) {
            // Fallback gracefully if ServiceLoader context fails
        }
    }

    private ExpressionRegistry() {
    }

    /**
     * Registers an {@link ExpressionTokenEvaluator} in the global expression registry.
     *
     * @param evaluator the evaluator instance to register
     */
    public static void register(ExpressionTokenEvaluator evaluator) {
        if (evaluator == null) {
            return;
        }
        for (String token : evaluator.supportedTokens()) {
            REGISTRY.put(token.toUpperCase(), evaluator);
        }
    }

    /**
     * Resolves an evaluator by token name (case-insensitive).
     *
     * @param tokenName the token name (e.g. "CURRENT_DATE", "UUID")
     * @return the registered evaluator, or {@code null} if not registered
     */
    public static ExpressionTokenEvaluator get(String tokenName) {
        if (tokenName == null || tokenName.isBlank()) {
            return null;
        }
        return REGISTRY.get(tokenName.trim().toUpperCase());
    }

    /**
     * Returns an unmodifiable map of registered token names to evaluators.
     *
     * @return map of registered tokens
     */
    public static Map<String, ExpressionTokenEvaluator> getRegisteredTokens() {
        return Collections.unmodifiableMap(REGISTRY);
    }
}
