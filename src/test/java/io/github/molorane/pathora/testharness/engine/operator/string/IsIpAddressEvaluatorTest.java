package io.github.molorane.pathora.testharness.engine.operator.string;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IsIpAddressEvaluatorTest {

    private IsIpAddressEvaluator operator;

    @BeforeEach
    void setUp() {
        operator = new IsIpAddressEvaluator();
    }

    @Test
    @DisplayName("PASS: valid IPv4 and IPv6 strings")
    void shouldPassWhenValidIp() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.ip", "192.168.1.1", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.ip", "127.0.0.1", null, true));
        assertThatNoException().isThrownBy(() -> operator.apply("$.ip", "::1", null, true));
    }

    @Test
    @DisplayName("FAIL: invalid IP string")
    void shouldFailWhenInvalidIp() {
        assertThatThrownBy(() -> operator.apply("$.ip", "999.999.999.999", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_IP_ADDRESS failed");

        assertThatThrownBy(() -> operator.apply("$.ip", "not-an-ip", null, true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("IS_IP_ADDRESS failed");
    }
}

