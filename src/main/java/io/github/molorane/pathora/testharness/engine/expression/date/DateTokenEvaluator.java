package io.github.molorane.pathora.testharness.engine.expression.date;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionOffsetUtils;
import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Evaluates {@code $CURRENT_DATE} and {@code $TODAY} expression tokens.
 */
public class DateTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("CURRENT_DATE", "TODAY");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        LocalDate date = PathoraClock.today();
        if (context.offsetStr() != null && !context.offsetStr().isBlank()) {
            date = ExpressionOffsetUtils.applyDateOffsets(date, context.offsetStr());
        }
        if (context.formatPattern() != null && !context.formatPattern().isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(context.formatPattern().trim());
            return date.format(formatter);
        }
        return date.toString();
    }
}
