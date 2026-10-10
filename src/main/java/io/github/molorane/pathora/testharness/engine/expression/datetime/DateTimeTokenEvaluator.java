package io.github.molorane.pathora.testharness.engine.expression.datetime;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionOffsetUtils;
import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Evaluates {@code $CURRENT_DATETIME} and {@code $NOW} expression tokens.
 */
public class DateTimeTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("CURRENT_DATETIME", "NOW");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        ZonedDateTime zdt = PathoraClock.nowZoned();
        if (context.offsetStr() != null && !context.offsetStr().isBlank()) {
            zdt = ExpressionOffsetUtils.applyZonedDateTimeOffsets(zdt, context.offsetStr());
        }
        if (context.formatPattern() != null && !context.formatPattern().isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(context.formatPattern().trim());
            return zdt.format(formatter);
        }
        return zdt.toInstant().toString();
    }
}
