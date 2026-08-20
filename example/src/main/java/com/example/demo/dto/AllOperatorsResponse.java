package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public record AllOperatorsResponse(
        // Scalar
        int score,
        int negativeScore,
        String status,
        double temperature,
        double rating,

        // String
        String productCode,
        String email,
        String versionStr,

        // Date
        String pastDate,
        String futureDate,
        String recentPastDateTime,
        String distantFutureDateTime,
        String baseDate,
        String earlierDate,
        String laterDate,
        String dtEarlier,
        String dtLater,

        // Duration
        String startDate,
        String endDate,

        // Structural
        String existingKey,
        boolean activeFlag,

        // Array
        List<Integer> numbers,
        List<Integer> uniqueNumbers,
        List<String> singleElementList,
        List<String> emptyList,
        List<String> fruits,
        List<String> singleFruitList,
        List<Map<String, Object>> users,
        String category,

        // Object
        Map<String, Object> userInfo,
        Map<String, Object> metrics,

        // Money
        double exactAmount,
        Map<String, Object> currencyAmount,
        double largeBalance,
        double smallFee,
        double thresholdAmount,
        double interestAccrual
) {
}
