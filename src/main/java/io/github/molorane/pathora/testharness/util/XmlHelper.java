package io.github.molorane.pathora.testharness.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class XmlHelper {

    private static volatile ObjectMapper xmlMapper;

    private XmlHelper() {
    }

    public static boolean isXml(String content) {
        return content != null && content.trim().startsWith("<");
    }

    public static ObjectMapper getXmlMapper() {
        if (xmlMapper == null) {
            synchronized (XmlHelper.class) {
                if (xmlMapper == null) {
                    try {
                        Class<?> clazz = Class.forName("com.fasterxml.jackson.dataformat.xml.XmlMapper");
                        xmlMapper = (ObjectMapper) clazz.getDeclaredConstructor().newInstance();
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException(
                                "XML request support requires 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml' on your classpath. " +
                                        "Please add this dependency to your project's pom.xml or build.gradle.", e
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate XmlMapper", e);
                    }
                }
            }
        }
        return xmlMapper;
    }
}
