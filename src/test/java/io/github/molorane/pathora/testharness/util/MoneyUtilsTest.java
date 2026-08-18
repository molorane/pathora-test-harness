package io.github.molorane.pathora.testharness.util;

import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyUtilsTest {

    @Test
    @DisplayName("PASS: extract BigDecimal from numbers and strings")
    void shouldExtractAmount() {
        assertThat(MoneyUtils.extractAmount(100)).isEqualTo(new BigDecimal("100"));
        assertThat(MoneyUtils.extractAmount(100.50)).isEqualTo(new BigDecimal("100.5"));
        assertThat(MoneyUtils.extractAmount("123.45")).isEqualTo(new BigDecimal("123.45"));
        assertThat(MoneyUtils.extractAmount(Map.of("amount", 250.75))).isEqualTo(new BigDecimal("250.75"));
    }

    @Test
    @DisplayName("PASS: extract currency code from maps and strings")
    void shouldExtractCurrency() {
        assertThat(MoneyUtils.extractCurrency(Map.of("currency", "USD"))).isEqualTo("USD");
        assertThat(MoneyUtils.extractCurrency("100.50 EUR")).isEqualTo("EUR");
    }

    @Test
    @DisplayName("PASS: validate currency match")
    void shouldValidateCurrencyMatch() {
        assertThatNoException().isThrownBy(() ->
                MoneyUtils.validateCurrencyMatch("$.amount", Map.of("currency", "USD"), Map.of("currency", "USD"), AssertionOperator.MONEY_EQUALS));

        assertThatThrownBy(() ->
                MoneyUtils.validateCurrencyMatch("$.amount", Map.of("currency", "USD"), Map.of("currency", "EUR"), AssertionOperator.MONEY_EQUALS))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("Currency mismatch");
    }
}
