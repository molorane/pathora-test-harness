package io.github.molorane.pathora.testharness.engine.expression;

import io.github.molorane.pathora.testharness.engine.expression.base64.Base64DecodeTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.base64.Base64EncodeTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.date.DateBoundaryTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.date.DateTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.datetime.DateTimeBoundaryTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.datetime.DateTimeTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.env.EnvTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.epoch.EpochMillisTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.epoch.EpochSecondsTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.hash.HashMd5TokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.hash.HashSha256TokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.math.MathTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomAlphanumericTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomBooleanTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomDecimalTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomEmailTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.random.RandomIntTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.string.LowercaseTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.string.UppercaseTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.sys.SysPropertyTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.time.TimeTokenEvaluator;
import io.github.molorane.pathora.testharness.engine.expression.url.UrlEncodeTokenEvaluator;
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
        // Automatically load classpath properties (pathora.properties, pathora.yml, pathora.yaml)
        io.github.molorane.pathora.testharness.config.PathoraConfigLoader.init();

        // Register default built-in date, time, and timestamp evaluators
        register(new DateTokenEvaluator());
        register(new DateBoundaryTokenEvaluator());
        register(new DateTimeTokenEvaluator());
        register(new DateTimeBoundaryTokenEvaluator());
        register(new TimeTokenEvaluator());
        register(new EpochMillisTokenEvaluator());
        register(new EpochSecondsTokenEvaluator());

        // Register default built-in random & identifier evaluators
        register(new UuidTokenEvaluator());
        register(new RandomIntTokenEvaluator());
        register(new RandomDecimalTokenEvaluator());
        register(new RandomAlphanumericTokenEvaluator());
        register(new RandomEmailTokenEvaluator());
        register(new RandomBooleanTokenEvaluator());

        // Register default built-in encoding, hashing, string transformation, math, env, and sys evaluators
        register(new Base64EncodeTokenEvaluator());
        register(new Base64DecodeTokenEvaluator());
        register(new UrlEncodeTokenEvaluator());
        register(new HashSha256TokenEvaluator());
        register(new HashMd5TokenEvaluator());
        register(new UppercaseTokenEvaluator());
        register(new LowercaseTokenEvaluator());
        register(new MathTokenEvaluator());
        register(new EnvTokenEvaluator());
        register(new SysPropertyTokenEvaluator());

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
