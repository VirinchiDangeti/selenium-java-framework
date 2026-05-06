package com.framework.config;
import com.framework.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    // ─── Static block runs ONCE when class is first loaded ───
    static {
        loadProperties();
    }

    // ─── Private constructor prevents instantiation ───
    private ConfigReader() {}

    /**
     * Loads the config.properties file into Properties object.
     * Throws RuntimeException if file is not found.
     */
    private static void loadProperties() {
        try (FileInputStream fis =
                new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {

            properties.load(fis);
            log.info("✅ config.properties loaded successfully");

        } catch (IOException e) {
            log.error("❌ config.properties not found at: "
                + FrameworkConstants.CONFIG_FILE_PATH);
            throw new RuntimeException(
                "❌ Failed to load config.properties! " +
                "Check path: " + FrameworkConstants.CONFIG_FILE_PATH, e
            );
        }
    }

    /**
     * Returns String value for a given key.
     * Throws RuntimeException if key not found.
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException(
                "❌ Key '" + key + "' not found in config.properties!"
            );
        }
        return value.trim();
    }

    /**
     * Returns int value for a given key.
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Returns boolean value for a given key.
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    /**
     * Returns value or a default if key not found.
     */
    public static String getOrDefault(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return (value != null && !value.trim().isEmpty())
            ? value.trim()
            : defaultValue;
    }
}