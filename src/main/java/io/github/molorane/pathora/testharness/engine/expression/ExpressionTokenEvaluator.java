package io.github.molorane.pathora.testharness.engine.expression;

import java.util.Set;

/**
 * Strategy interface for resolving dynamic expression tokens in Pathora Test Harness.
 *
 * <p>Implementations of this interface register supported token names (e.g. {@code CURRENT_DATE},
 * {@code UUID}) and evaluate dynamic expressions according to the provided {@link TokenContext}.</p>
 */
public interface ExpressionTokenEvaluator {

    /**
     * Returns the set of token names or aliases supported by this evaluator (case-insensitive).
     *
     * @return set of supported token names
     */
    Set<String> supportedTokens();

    /**
     * Evaluates the expression token based on the provided context.
     *
     * @param context contextual information about the expression token
     * @return the evaluated result object (String, Long, etc.)
     */
    Object evaluate(TokenContext context);

    /**
     * Contextual information passed to an evaluator during token resolution.
     *
     * @param tokenName     the token name as parsed (e.g. "CURRENT_DATE")
     * @param offsetStr     the optional offset expression string (e.g. "+30d", "-25y")
     * @param formatPattern the optional custom format specifier (e.g. "dd/MM/yyyy")
     * @param allowNumeric  whether a numeric primitive wrapper (e.g. Long) is allowed for standalone expressions
     */
    record TokenContext(
        String tokenName,
        String offsetStr,
        String formatPattern,
        boolean allowNumeric
    ) { }
}
