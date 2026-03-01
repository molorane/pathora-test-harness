package za.co.pathora.testharness.engine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.pathora.testharness.exception.HarnessAssertionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegexMatchOperatorTest {

    private RegexMatchOperator operator;

    @BeforeEach
    void setUp() {
        operator = new RegexMatchOperator();
    }

    // ── PASS cases ──

    @Test
    @DisplayName("PASS: reference ID matches pattern")
    void shouldPassWhenReferenceIdMatches() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.refId", "REF-1234-56789", "^REF-\\d{4}-\\d{5}$", true));
    }

    @Test
    @DisplayName("PASS: email matches pattern")
    void shouldPassWhenEmailMatches() {
        assertThatNoException()
                .isThrownBy(() -> operator.apply("$.email", "user@example.com", "^[\\w.]+@[\\w.]+\\.[a-z]{2,}$", true));
    }

    @Test
    @DisplayName("PASS: numeric string matches digit pattern")
    void shouldPassWhenDigitsMatch() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", "12345", "^\\d+$", true));
    }

    @Test
    @DisplayName("PASS: UUID matches pattern")
    void shouldPassWhenUuidMatches() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.id", "550e8400-e29b-41d4-a716-446655440000",
                "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", true));
    }

    @Test
    @DisplayName("PASS: any non-empty string")
    void shouldPassWithDotPlus() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.name", "anything", ".+", true));
    }

    @Test
    @DisplayName("PASS: numeric actual converted to string")
    void shouldPassWithNumericActual() {
        assertThatNoException().isThrownBy(() -> operator.apply("$.code", 12345, "^\\d+$", true));
    }

    // ── FAIL cases ──

    @Test
    @DisplayName("FAIL: reference ID does not match pattern")
    void shouldFailWhenPatternDoesNotMatch() {
        assertThatThrownBy(() -> operator.apply("$.refId", "INVALID-ID", "^REF-\\d{4}-\\d{5}$", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("REGEX_MATCH failed");
    }

    @Test
    @DisplayName("FAIL: partial match is not enough — must be full match")
    void shouldFailOnPartialMatch() {
        assertThatThrownBy(() -> operator.apply("$.refId", "REF-1234-56789-extra", "^REF-\\d{4}-\\d{5}$", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("REGEX_MATCH failed");
    }

    @Test
    @DisplayName("FAIL: empty string against non-empty pattern")
    void shouldFailWhenActualIsEmpty() {
        assertThatThrownBy(() -> operator.apply("$.field", "", ".+", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("REGEX_MATCH failed");
    }

    // ── Edge / error cases ──

    @Test
    @DisplayName("FAIL: expected is not a string")
    void shouldFailWhenExpectedIsNotString() {
        assertThatThrownBy(() -> operator.apply("$.field", "value", 123, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("REGEX_MATCH requires a string pattern");
    }

    @Test
    @DisplayName("FAIL: invalid regex pattern")
    void shouldFailWithInvalidRegex() {
        assertThatThrownBy(() -> operator.apply("$.field", "value", "[invalid(", true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalid regex pattern");
    }

    @Test
    @DisplayName("PASS: case-sensitive match by default")
    void shouldBeCaseSensitive() {
        assertThatThrownBy(() -> operator.apply("$.status", "approved", "^APPROVED$", true))
                .isInstanceOf(HarnessAssertionException.class)
                .hasMessageContaining("REGEX_MATCH failed");
    }
}
