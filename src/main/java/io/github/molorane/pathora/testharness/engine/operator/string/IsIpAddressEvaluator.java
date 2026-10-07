package io.github.molorane.pathora.testharness.engine.operator.string;

import io.github.molorane.pathora.testharness.engine.operator.AssertionEvaluator;
import io.github.molorane.pathora.testharness.exception.HarnessAssertionException;
import io.github.molorane.pathora.testharness.model.AssertionOperator;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Operator: {@code IS_IP_ADDRESS}
 *
 * <p>Validates that the extracted string is a valid IPv4 or IPv6 network address.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * {
 *   "JsonPath": "$.outputData.clientIp",
 *   "Operator": "IS_IP_ADDRESS"
 * }
 * }</pre>
 *
 * @see AssertionOperator#IS_IP_ADDRESS
 */
public class IsIpAddressEvaluator implements AssertionEvaluator {

    private static final Pattern IPV4_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$"
    );
    private static final Pattern IPV6_HEX_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");

    @Override
    public AssertionOperator operator() {
        return AssertionOperator.IS_IP_ADDRESS;
    }

    @Override
    public void apply(String path, Object actual, Object expected, boolean pathExists) {
        String actualStr = StringHelper.toSingleString(actual, path);

        boolean validIp = false;
        if (actualStr != null && !actualStr.trim().isEmpty()) {
            String candidate = actualStr.trim();
            if (IPV4_PATTERN.matcher(candidate).matches()) {
                validIp = true;
            } else if (candidate.contains(":") && IPV6_HEX_PATTERN.matcher(candidate).matches()) {
                try {
                    InetAddress address = InetAddress.getByName(candidate);
                    if (address instanceof Inet6Address) {
                        validIp = true;
                    }
                } catch (UnknownHostException ignored) {
                }
            }
        }

        if (!validIp) {
            throw new HarnessAssertionException(
                AssertionOperator.IS_IP_ADDRESS,
                path,
                "valid IP address",
                actual,
                "IS_IP_ADDRESS failed at " + path +
                    ". Expected valid IPv4 or IPv6 address, Actual: " + actualStr);
        }
    }
}

