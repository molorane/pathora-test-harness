package io.github.molorane.pathora.testharness.engine.expression.date;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionOffsetUtils;
import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

/**
 * Evaluates date boundary tokens (e.g. {@code $START_OF_MONTH}, {@code $END_OF_MONTH},
 * {@code $FIRST_DAY_OF_NEXT_MONTH}, {@code $START_OF_YEAR}, {@code $END_OF_YEAR}).
 */
public class DateBoundaryTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of(
        "START_OF_MONTH",
        "END_OF_MONTH",
        "FIRST_DAY_OF_NEXT_MONTH",
        "START_OF_YEAR",
        "END_OF_YEAR"
    );

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        LocalDate today = PathoraClock.today();

        LocalDate baseDate = switch (context.tokenName().toUpperCase()) {
            case "START_OF_MONTH" -> today.with(TemporalAdjusters.firstDayOfMonth());
            case "END_OF_MONTH" -> today.with(TemporalAdjusters.lastDayOfMonth());
            case "FIRST_DAY_OF_NEXT_MONTH" -> today.with(TemporalAdjusters.firstDayOfNextMonth());
            case "START_OF_YEAR" -> today.with(TemporalAdjusters.firstDayOfYear());
            case "END_OF_YEAR" -> today.with(TemporalAdjusters.lastDayOfYear());
            default -> throw new IllegalArgumentException("Unknown date boundary token: $" + context.tokenName());
        };

        if (context.offsetStr() != null && !context.offsetStr().isBlank()) {
            baseDate = ExpressionOffsetUtils.applyDateOffsets(baseDate, context.offsetStr());
        }

        if (context.formatPattern() != null && !context.formatPattern().isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(context.formatPattern().trim());
            return baseDate.format(formatter);
        }

        return baseDate.toString();
    }
}
