package io.github.molorane.pathora.testharness.engine.expression.time;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Evaluates {@code $CURRENT_TIME} expression tokens.
 */
public class TimeTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("CURRENT_TIME");
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        LocalTime time = PathoraClock.nowZoned().toLocalTime();
        if (context.formatPattern() != null && !context.formatPattern().isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(context.formatPattern().trim());
            return time.format(formatter);
        }
        return time.format(DEFAULT_FORMATTER);
    }
}
