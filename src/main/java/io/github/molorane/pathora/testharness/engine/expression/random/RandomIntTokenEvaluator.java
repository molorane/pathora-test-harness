package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Evaluates {@code $RANDOM_INT} and {@code $RANDOM_INTEGER} expression tokens.
 *
 * <p>Supports range bounds specified via pattern parameter (e.g. {@code {{$RANDOM_INT:1000:9999}}}).</p>
 */
public class RandomIntTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("RANDOM_INT", "RANDOM_INTEGER");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        int[] bounds = parseBounds(context);
        int min = bounds[0];
        int max = bounds[1];

        int randomVal = ThreadLocalRandom.current().nextInt(min, max == Integer.MAX_VALUE ? max : max + 1);
        return context.allowNumeric() ? randomVal : String.valueOf(randomVal);
    }

    private int[] parseBounds(TokenContext context) {
        int min = 0;
        int max = Integer.MAX_VALUE - 1;

        String param = extractParam(context);
        if (param.isBlank()) {
            return new int[]{min, max};
        }

        String[] parts = param.split("[:,-]");
        if (parts.length >= 2) {
            try {
                min = Integer.parseInt(parts[0].trim());
                max = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException ignored) {
            }
        } else if (parts.length == 1) {
            try {
                max = Integer.parseInt(parts[0].trim());
            } catch (NumberFormatException ignored) {
            }
        }

        return min > max ? new int[]{max, min} : new int[]{min, max};
    }

    private String extractParam(TokenContext context) {
        if (context.formatPattern() != null && !context.formatPattern().isBlank()) {
            return context.formatPattern().trim();
        }
        if (context.offsetStr() != null) {
            return context.offsetStr().trim();
        }
        return "";
    }
}
