package io.github.molorane.pathora.testharness.engine;

import io.github.molorane.pathora.testharness.engine.operator.OperatorAssertion;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.util.EnumMap;
import java.util.Map;

public final class OperatorRegistry {

    private static final Map<AssertionOperator, String> CLASS_MAP = new EnumMap<>(AssertionOperator.class);

    static {
        CLASS_MAP.put(AssertionOperator.EQUALS, "EqualsOperator");
        CLASS_MAP.put(AssertionOperator.NOT_EQUALS, "NotEqualsOperator");
        CLASS_MAP.put(AssertionOperator.GREATER_THAN, "GreaterThanOperator");
        CLASS_MAP.put(AssertionOperator.LESS_THAN, "LessThanOperator");
        CLASS_MAP.put(AssertionOperator.BETWEEN, "BetweenOperator");
        CLASS_MAP.put(AssertionOperator.GREATER_THAN_OR_EQUALS, "GreaterThanOrEqualsOperator");
        CLASS_MAP.put(AssertionOperator.LESS_THAN_OR_EQUALS, "LessThanOrEqualsOperator");
        CLASS_MAP.put(AssertionOperator.REGEX_MATCH, "RegexMatchOperator");
        CLASS_MAP.put(AssertionOperator.STARTS_WITH, "StartsWithOperator");
        CLASS_MAP.put(AssertionOperator.ENDS_WITH, "EndsWithOperator");
        CLASS_MAP.put(AssertionOperator.DATE_BEFORE, "DateBeforeOperator");
        CLASS_MAP.put(AssertionOperator.DATE_AFTER, "DateAfterOperator");
        CLASS_MAP.put(AssertionOperator.DATETIME_BEFORE, "DateTimeBeforeOperator");
        CLASS_MAP.put(AssertionOperator.DATETIME_AFTER, "DateTimeAfterOperator");
        CLASS_MAP.put(AssertionOperator.DATE_BEFORE_NOW, "DateBeforeNowOperator");
        CLASS_MAP.put(AssertionOperator.DATE_AFTER_NOW, "DateAfterNowOperator");
        CLASS_MAP.put(AssertionOperator.DATE_WITHIN_LAST, "DateWithinLastOperator");
        CLASS_MAP.put(AssertionOperator.DATE_WITHIN_NEXT, "DateWithinNextOperator");
        CLASS_MAP.put(AssertionOperator.DURATION_BETWEEN, "DurationBetweenDatesOperator");
        CLASS_MAP.put(AssertionOperator.DURATION_EQUALS, "DurationEqualsOperator");
        CLASS_MAP.put(AssertionOperator.DURATION_GREATER_THAN, "DurationGreaterThanOperator");
        CLASS_MAP.put(AssertionOperator.DURATION_LESS_THAN, "DurationLessThanOperator");
        CLASS_MAP.put(AssertionOperator.DATE_AFTER_DURATION, "DateAfterDurationOperator");
        CLASS_MAP.put(AssertionOperator.DATE_BEFORE_DURATION, "DateBeforeDurationOperator");
        CLASS_MAP.put(AssertionOperator.EXISTS, "ExistsOperator");
        CLASS_MAP.put(AssertionOperator.ARRAY_SIZE_EQUALS, "ArraySizeEqualsOperator");
        CLASS_MAP.put(AssertionOperator.ARRAY_CONTAINS, "ArrayContainsOperator");
        CLASS_MAP.put(AssertionOperator.ARRAY_CONTAINS_ONLY_VALUES, "ArrayContainsOnlyValuesOperator");
        CLASS_MAP.put(AssertionOperator.ARRAY_CONTAINS_ONLY_ONE_VALUE, "ArrayContainsOnlyOneValueOperator");
        CLASS_MAP.put(AssertionOperator.ARRAY_CONTAINS_OBJECT_WITH_FIELDS, "ArrayContainsObjectWithFieldsOperator");
        CLASS_MAP.put(AssertionOperator.ALL_MATCH, "AllMatchOperator");
        CLASS_MAP.put(AssertionOperator.CONTAINS_ANY, "ContainsAnyOperator");
        CLASS_MAP.put(AssertionOperator.CONTAINS_ALL, "ContainsAllOperator");
        CLASS_MAP.put(AssertionOperator.ARRAY_IS_EMPTY, "ArrayIsEmptyOperator");
        CLASS_MAP.put(AssertionOperator.UNIQUE_ELEMENTS, "UniqueElementsOperator");
        CLASS_MAP.put(AssertionOperator.OBJECT_CONTAINS_FIELDS, "ObjectContainsFieldsOperator");
        CLASS_MAP.put(AssertionOperator.OBJECT_CONTAINS_FIELDS_IGNORE_NULLS,
                "ObjectContainsFieldsIgnoreNullsOperator");
        CLASS_MAP.put(AssertionOperator.HAS_KEYS, "HasKeysOperator");
        CLASS_MAP.put(AssertionOperator.FIELD_EQUALS_OTHER_FIELD, "FieldEqualsOtherFieldOperator");
    }

    private OperatorRegistry() {
    }

    public static Map<AssertionOperator, OperatorAssertion> loadOperators() {
        Map<AssertionOperator, OperatorAssertion> map = new EnumMap<>(AssertionOperator.class);
        String packageName = "io.github.molorane.pathora.testharness.engine.operator.";
        for (Map.Entry<AssertionOperator, String> entry : CLASS_MAP.entrySet()) {
            try {
                Class<?> clazz = Class.forName(packageName + entry.getValue());
                OperatorAssertion instance = (OperatorAssertion) clazz.getDeclaredConstructor().newInstance();
                map.put(entry.getKey(), instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate operator: " + entry.getValue(), e);
            }
        }
        return map;
    }
}
