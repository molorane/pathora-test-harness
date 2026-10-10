package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Evaluates {@code $RANDOM_EMAIL} expression tokens.
 */
public class RandomEmailTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("RANDOM_EMAIL");
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        StringBuilder sb = new StringBuilder("user_");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 6; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        sb.append("@test.com");
        return sb.toString();
    }
}
