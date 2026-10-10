package io.github.molorane.pathora.testharness.engine.expression.math;

import io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

/**
 * Evaluates {@code $MATH:expression} expression tokens (e.g. {@code {{$MATH: 100 * 1.15}}}).
 */
public class MathTokenEvaluator implements ExpressionTokenEvaluator {

    private static final Set<String> TOKENS = Set.of("MATH");

    @Override
    public Set<String> supportedTokens() {
        return TOKENS;
    }

    @Override
    public Object evaluate(TokenContext context) {
        String expr = context.formatPattern();
        if (expr == null || expr.isBlank()) {
            expr = context.offsetStr();
        }
        if (expr == null || expr.isBlank()) {
            return context.allowNumeric() ? 0 : "0";
        }

        BigDecimal result = parseAndEvaluate(expr.trim());
        BigDecimal stripped = result.stripTrailingZeros();

        if (context.allowNumeric()) {
            if (stripped.scale() <= 0) {
                try {
                    return stripped.longValueExact();
                } catch (ArithmeticException ignored) {
                    return stripped.doubleValue();
                }
            }
            return stripped.doubleValue();
        }

        return stripped.toPlainString();
    }

    private BigDecimal parseAndEvaluate(String expr) {
        Parser parser = new Parser(expr);
        return parser.parse();
    }

    private static class Parser {
        private final String str;
        private int pos = -1;
        private int ch;

        Parser(String str) {
            this.str = str;
        }

        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') {
                nextChar();
            }
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        BigDecimal parse() {
            nextChar();
            BigDecimal x = parseExpression();
            if (pos < str.length()) {
                throw new IllegalArgumentException("Unexpected character in math expression: " + (char) ch);
            }
            return x;
        }

        BigDecimal parseExpression() {
            BigDecimal x = parseTerm();
            while (true) {
                if (eat('+')) {
                    x = x.add(parseTerm());
                } else if (eat('-')) {
                    x = x.subtract(parseTerm());
                } else {
                    return x;
                }
            }
        }

        BigDecimal parseTerm() {
            BigDecimal x = parseFactor();
            while (true) {
                if (eat('*')) {
                    x = x.multiply(parseFactor());
                } else if (eat('/')) {
                    BigDecimal divisor = parseFactor();
                    x = x.divide(divisor, 10, RoundingMode.HALF_UP);
                } else if (eat('%')) {
                    BigDecimal divisor = parseFactor();
                    x = x.remainder(divisor);
                } else {
                    return x;
                }
            }
        }

        BigDecimal parseFactor() {
            BigDecimal x = parseBase();
            while (true) {
                if (eat('^')) {
                    BigDecimal power = parseBase();
                    x = BigDecimal.valueOf(Math.pow(x.doubleValue(), power.doubleValue()));
                } else {
                    return x;
                }
            }
        }

        BigDecimal parseBase() {
            if (eat('+')) {
                return parseBase();
            }
            if (eat('-')) {
                return parseBase().negate();
            }

            BigDecimal x;
            int startPos = this.pos;
            if (eat('(')) {
                x = parseExpression();
                eat(')');
            } else if (ch >= '0' && ch <= '9' || ch == '.') {
                while (ch >= '0' && ch <= '9' || ch == '.') {
                    nextChar();
                }
                x = new BigDecimal(str.substring(startPos, this.pos));
            } else {
                throw new IllegalArgumentException("Unexpected character in math expression: " + (char) ch);
            }

            return x;
        }
    }
}
