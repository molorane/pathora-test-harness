/**
 * Dynamic expression language engine, registry, and token evaluator strategies for Pathora Test Harness.
 *
 * <p>Provides dynamic evaluation of temporal tokens ({@code $CURRENT_DATE}, {@code $CURRENT_DATETIME}),
 * random generators ({@code $RANDOM_DECIMAL}, {@code $RANDOM_INT}, {@code $UUID}), math operations ({@code $MATH}),
 * security hashing/encoding ({@code $BASE64_ENCODE}, {@code $HASH_SHA256}), environment variables ({@code $ENV}),
 * system properties ({@code $SYS}), and classpath config properties ({@code $PROP}, {@code $PATHORA}, {@code $CONFIG}).</p>
 *
 * <p>Consumers can extend the expression language by implementing {@link io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator}
 * and registering evaluators programmatically or via Java {@link java.util.ServiceLoader} SPI.</p>
 */
package io.github.molorane.pathora.testharness.engine.expression;
