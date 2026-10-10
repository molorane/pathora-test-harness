package io.github.molorane.pathora.testharness.engine.expression.uuid;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;
import java.util.UUID;

/**
 * Evaluates {@code $UUID} and {@code $RANDOM_UUID} expression tokens to produce a random UUID v4 string.
 */
public class UuidTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("UUID", "RANDOM_UUID");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        return UUID.randomUUID().toString();
    }
}
