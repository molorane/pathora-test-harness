package io.github.molorane.pathora.testharness.engine.expression.epoch;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionOffsetUtils;
import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;
import io.github.molorane.pathora.testharness.util.PathoraClock;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Evaluates {@code $EPOCH_SECONDS} expression tokens.
 */
public class EpochSecondsTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("EPOCH_SECONDS");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        Instant instant = PathoraClock.instant();
        if (context.offsetStr() != null && !context.offsetStr().isBlank()) {
            ZonedDateTime zdt = instant.atZone(PathoraClock.getZoneId());
            zdt = ExpressionOffsetUtils.applyZonedDateTimeOffsets(zdt, context.offsetStr());
            instant = zdt.toInstant();
        }
        long seconds = instant.getEpochSecond();
        return context.allowNumeric() ? seconds : String.valueOf(seconds);
    }
}
