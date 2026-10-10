package io.github.molorane.pathora.testharness.engine.expression.datetime;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionOffsetUtils;
import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Evaluates datetime boundary tokens (e.g. {@code $START_OF_DAY}, {@code $END_OF_DAY}).
 */
public class DateTimeBoundaryTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("START_OF_DAY", "END_OF_DAY");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        ZonedDateTime nowZoned = PathoraClock.nowZoned();

        ZonedDateTime baseZdt = switch (context.tokenName().toUpperCase()) {
            case "START_OF_DAY" -> nowZoned.with(LocalTime.MIN);
            case "END_OF_DAY" -> nowZoned.with(LocalTime.MAX);
            default -> throw new IllegalArgumentException("Unknown datetime boundary token: $" + context.tokenName());
        };

        if (context.offsetStr() != null && !context.offsetStr().isBlank()) {
            baseZdt = ExpressionOffsetUtils.applyZonedDateTimeOffsets(baseZdt, context.offsetStr());
        }

        if (context.formatPattern() != null && !context.formatPattern().isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(context.formatPattern().trim());
            return baseZdt.format(formatter);
        }

        return baseZdt.toInstant().toString();
    }
}
