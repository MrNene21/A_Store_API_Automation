package org.astore.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE_PATH = "src/main/resources/configurations.properties";
    private static Properties properties;

    static {
        properties = loadProperties();
    }

    private static Properties loadProperties() {
        try (InputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            System.err.println("Error loading configuration.properties file: " + e.getMessage());
            return null;
        }
    }

    public static String getBaseURI() {
        return properties.getProperty("baseURI");
    }
}
