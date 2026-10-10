package io.github.molorane.pathora.testharness.config;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Automatically discovers and loads configuration properties from classpath resources
 * ({@code pathora.properties}, {@code pathora.yml}, or {@code pathora.yaml}).
 *
 * <p>Loaded properties are registered into JVM system properties (without overwriting explicitly
 * provided system properties), making them seamlessly available to expression tokens such as
 * {@code $SYS:property.name}.</p>
 */
public final class PathoraConfigLoader {

    private static boolean initialized = false;

    private PathoraConfigLoader() {
    }

    /**
     * Initializes configuration loading from the classpath if not already initialized.
     */
    public static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        loadConfig();
    }

    /**
     * Resets the initialization state (primarily for testing).
     */
    public static synchronized void reset() {
        initialized = false;
    }

    private static void loadConfig() {
        Map<String, String> loadedProperties = new LinkedHashMap<>();

        loadPropertiesFile("pathora.properties", loadedProperties);
        loadYamlFile("pathora.yml", loadedProperties);
        loadYamlFile("pathora.yaml", loadedProperties);

        for (Map.Entry<String, String> entry : loadedProperties.entrySet()) {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void loadPropertiesFile(String filename, Map<String, String> out) {
        try (InputStream is = getResourceAsStream(filename)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                for (String name : props.stringPropertyNames()) {
                    out.put(name, props.getProperty(name));
                }
            }
        } catch (Exception ignored) {
            // Silently ignore if file is missing or unparseable
        }
    }

    private static void loadYamlFile(String filename, Map<String, String> out) {
        try (InputStream is = getResourceAsStream(filename)) {
            if (is != null) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                @SuppressWarnings("unchecked")
                Map<String, Object> yamlMap = mapper.readValue(is, Map.class);
                if (yamlMap != null) {
                    flattenYaml("", yamlMap, out);
                }
            }
        } catch (Exception ignored) {
            // Silently ignore if file is missing or unparseable
        }
    }

    @SuppressWarnings("unchecked")
    private static void flattenYaml(String prefix, Map<String, Object> map, Map<String, String> out) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object val = entry.getValue();
            if (val instanceof Map<?, ?> childMap) {
                flattenYaml(key, (Map<String, Object>) childMap, out);
            } else if (val != null) {
                out.put(key, String.valueOf(val));
            }
        }
    }

    private static InputStream getResourceAsStream(String name) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            InputStream is = cl.getResourceAsStream(name);
            if (is != null) {
                return is;
            }
        }
        return PathoraConfigLoader.class.getClassLoader().getResourceAsStream(name);
    }
}
