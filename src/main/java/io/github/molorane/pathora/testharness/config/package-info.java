/**
 * Classpath configuration property discovery and initialization for Pathora Test Harness.
 *
 * <p>Discovers and parses {@code pathora.properties}, {@code pathora.yml}, or {@code pathora.yaml}
 * files on the classpath, flattening YAML hierarchies into dotted keys and registering them as
 * system properties for dynamic expression resolution.</p>
 */
package io.github.molorane.pathora.testharness.config;
