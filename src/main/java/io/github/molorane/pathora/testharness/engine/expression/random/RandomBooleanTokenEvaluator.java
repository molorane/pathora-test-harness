package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Evaluates {@code $RANDOM_BOOLEAN} and {@code $RANDOM_BOOL} expression tokens dynamically.
 */
public class RandomBooleanTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("RANDOM_BOOLEAN", "RANDOM_BOOL");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        boolean value = ThreadLocalRandom.current().nextBoolean();
        return context.allowNumeric() ? value : String.valueOf(value);
    }
}
