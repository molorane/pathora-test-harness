package io.github.molorane.pathora.testharness.engine.operator;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import io.github.molorane.pathora.testharness.util.AssertionUtils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexMatchOperator implements AssertionEvaluator {

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.REGEX_MATCH;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {

        Object normalizedActual = AssertionUtils.normalizeResult(actual, path);

        if (!(expected instanceof String regex)) {
            throw new IllegalArgumentException(
                    "REGEX_MATCH requires a string pattern as Value at " + path +
                            ". Got: " + expected);
        }

        String actualStr = String.valueOf(normalizedActual);

        Pattern pattern;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException(
                    "REGEX_MATCH has invalid regex pattern at " + path +
                            ": " + regex,
                    e);
        }

        if (!pattern.matcher(actualStr).matches()) {
            throw new HarnessAssertionException(
                    AssertionOperator.REGEX_MATCH,
                    path,
                    regex,
                    actualStr,
                    "REGEX_MATCH failed at " + path +
                            ". Pattern: " + regex +
                            ", Actual: " + actualStr);
        }
    }
}
