package io.github.molorane.pathora.testharness.util;

import tools.jackson.databind.ObjectMapper;

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
                        Class<?> clazz = Class.forName("tools.jackson.dataformat.xml.XmlMapper");
                        xmlMapper = (ObjectMapper) clazz.getDeclaredConstructor().newInstance();
                    } catch (ClassNotFoundException e) {
                        try {
                            Class<?> clazz = Class.forName("com.fasterxml.jackson.dataformat.xml.XmlMapper");
                            xmlMapper = (ObjectMapper) clazz.getDeclaredConstructor().newInstance();
                        } catch (ClassNotFoundException e2) {
                            throw new IllegalStateException(
                                    "XML request support requires Jackson XML Dataformat on your classpath. " +
                                            "Please add 'tools.jackson.dataformat:jackson-dataformat-xml' to your project.", e2
                            );
                        } catch (Exception e2) {
                            throw new RuntimeException("Failed to instantiate XmlMapper", e2);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate XmlMapper", e);
                    }
                }
            }
        }
        return xmlMapper;
    }
}
