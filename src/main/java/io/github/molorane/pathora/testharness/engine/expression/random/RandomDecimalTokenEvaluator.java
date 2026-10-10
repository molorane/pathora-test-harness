package io.github.molorane.pathora.testharness.engine.expression.random;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Evaluates {@code $RANDOM_DECIMAL} and {@code $RANDOM_DOUBLE} expression tokens.
 *
 * <p>Supports min, max, and scale parameters (e.g. {@code {{$RANDOM_DECIMAL:10.00:500.00:2}}}).</p>
 */
public class RandomDecimalTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("RANDOM_DECIMAL", "RANDOM_DOUBLE");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        double[] boundsAndScale = parseBoundsAndScale(context);
        double min = boundsAndScale[0];
        double max = boundsAndScale[1];
        int scale = (int) boundsAndScale[2];

        double randomRaw;
        if (Double.compare(min, max) == 0) {
            randomRaw = min;
        } else {
            randomRaw = min + (max - min) * ThreadLocalRandom.current().nextDouble();
        }

        BigDecimal bd = BigDecimal.valueOf(randomRaw).setScale(scale, RoundingMode.HALF_UP);

        if (context.allowNumeric()) {
            return bd.doubleValue();
        }
        return bd.toPlainString();
    }

    private double[] parseBoundsAndScale(TokenContext context) {
        double min = 0.0;
        double max = 100.0;
        int scale = 2;

        String param = extractParam(context);
        if (param.isBlank()) {
            return new double[]{min, max, scale};
        }

        String[] parts = param.split(":");
        if (parts.length >= 3) {
            try {
                min = Double.parseDouble(parts[0].trim());
                max = Double.parseDouble(parts[1].trim());
                scale = Integer.parseInt(parts[2].trim());
            } catch (NumberFormatException ignored) {
            }
        } else if (parts.length == 2) {
            try {
                min = Double.parseDouble(parts[0].trim());
                max = Double.parseDouble(parts[1].trim());
            } catch (NumberFormatException ignored) {
            }
        } else if (parts.length == 1) {
            try {
                max = Double.parseDouble(parts[0].trim());
            } catch (NumberFormatException ignored) {
            }
        }

        if (scale < 0) {
            scale = 0;
        }

        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }

        return new double[]{min, max, scale};
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
