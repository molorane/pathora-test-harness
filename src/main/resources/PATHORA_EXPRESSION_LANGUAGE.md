# Pathora Dynamic Expression Language Reference

Pathora Test Harness provides a dynamic expression language for request mutations and response assertions.

---

## Token Reference Catalog

| Category | Token | Alias / Syntax | Example Expression | Evaluated Output Example |
| :--- | :--- | :--- | :--- | :--- |
| **Temporal** | `CURRENT_DATE` | `TODAY` | `{{$CURRENT_DATE + 30d}}` | `2026-11-06` |
| **Temporal** | `CURRENT_DATETIME` | `NOW` | `{{$CURRENT_DATETIME + 2h}}` | `2026-10-07T16:30:00` |
| **Temporal** | `CURRENT_TIME` | | `{{$CURRENT_TIME}}` | `14:30:00` |
| **Temporal** | `EPOCH_MILLIS` | `TIMESTAMP` | `{{$EPOCH_MILLIS}}` | `1791374400000` |
| **Temporal** | `EPOCH_SECONDS` | | `{{$EPOCH_SECONDS}}` | `1791374400` |
| **Boundary** | `START_OF_DAY` | | `{{$START_OF_DAY}}` | `2026-10-07T00:00:00Z` |
| **Boundary** | `END_OF_DAY` | | `{{$END_OF_DAY}}` | `2026-10-07T23:59:59.999999999Z` |
| **Boundary** | `START_OF_MONTH` | | `{{$START_OF_MONTH}}` | `2026-10-01` |
| **Boundary** | `END_OF_MONTH` | | `{{$END_OF_MONTH}}` | `2026-10-31` |
| **Boundary** | `FIRST_DAY_OF_NEXT_MONTH` | | `{{$FIRST_DAY_OF_NEXT_MONTH}}` | `2026-11-01` |
| **Boundary** | `START_OF_YEAR` | | `{{$START_OF_YEAR}}` | `2026-01-01` |
| **Boundary** | `END_OF_YEAR` | | `{{$END_OF_YEAR}}` | `2026-12-31` |
| **Identifier** | `UUID` | | `{{$UUID}}` | `f8d24235-c39d-4fe1-a579-92d46ed30c07` |
| **Random** | `RANDOM_INT` | `min:max` | `{{$RANDOM_INT:1000:9999}}` | `4829` |
| **Random** | `RANDOM_DECIMAL` | `min:max:scale` | `{{$RANDOM_DECIMAL:10.00:500.00:2}}` | `142.75` |
| **Random** | `RANDOM_ALPHANUMERIC`| `len` | `REF-{{$RANDOM_ALPHANUMERIC:8}}` | `REF-Z8R2UBVL` |
| **Random** | `RANDOM_EMAIL` | | `{{$RANDOM_EMAIL}}` | `user_n34ty5@test.com` |
| **Random** | `RANDOM_BOOLEAN` | | `{{$RANDOM_BOOLEAN}}` | `true` |
| **Math** | `MATH` | `expr` | `{{$MATH: 100 * 1.15}}` | `115` |
| **Security** | `BASE64_ENCODE` | `text` | `{{$BASE64_ENCODE:admin:secret}}` | `YWRtaW46c2VjcmV0` |
| **Security** | `BASE64_DECODE` | `b64` | `{{$BASE64_DECODE:YWRtaW46c2VjcmV0}}` | `admin:secret` |
| **Security** | `URL_ENCODE` | `text` | `{{$URL_ENCODE:user@test.org}}` | `user%40test.org` |
| **Security** | `HASH_SHA256` | `text` | `{{$HASH_SHA256:secret123}}` | `8c6976e5b5410415bde...` |
| **Security** | `HASH_MD5` | `text` | `{{$HASH_MD5:secret123}}` | `5d7845ac6ee7cfff...` |
| **String** | `UPPERCASE` | `text` | `{{$UPPERCASE:manager}}` | `MANAGER` |
| **String** | `LOWERCASE` | `text` | `{{$LOWERCASE:MANAGER}}` | `manager` |
| **Config** | `ENV` | `VAR:default` | `{{$ENV:PATH:default_path}}` | `/usr/bin:...` |
| **Config** | `SYS` | `prop:default` | `{{$SYS:java.version}}` | `17.0.12` |
| **Config** | `PROP` | `PATHORA`, `CONFIG` | `{{$PROP:pathora.environment}}` | `DEMO_STAGING` |

---

## Classpath Property Auto-Discovery (`pathora.yml` / `pathora.properties`)

Pathora automatically scans for `pathora.properties`, `pathora.yml`, or `pathora.yaml` on the classpath at startup.
Nested YAML structures are flattened into dotted keys and made available via `{{$PROP:key}}`.

---

## Custom Expression Extensions (`ExpressionTokenEvaluator`)

Consumers can register custom expression tokens programmatically or via Java SPI:

- **Programmatic**: `ExpressionRegistry.register(new MyCustomEvaluator());`
- **SPI Auto-Discovery**: Descriptor file in `META-INF/services/io.github.molorane.pathora.testharness.engine.expression.ExpressionTokenEvaluator`
