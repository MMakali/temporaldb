package com.temporaldb.cli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Configuration for CLI.
 */
public class CLIConfig {
    private static final Logger logger = LoggerFactory.getLogger(CLIConfig.class);

    private final Properties properties;
    private final String configPath;

    public CLIConfig(String configPath) {
        this.configPath = configPath;
        this.properties = new Properties();
        loadConfig();
    }

    /**
     * Load configuration from file.
     */
    private void loadConfig() {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(configPath);
            if (java.nio.file.Files.exists(path)) {
                properties.load(new java.io.FileInputStream(configPath));
                logger.info("Loaded configuration from: {}", configPath);
            }
        } catch (Exception e) {
            logger.warn("Could not load configuration: {}", e.getMessage());
        }
    }

    /**
     * Get configuration property.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get configuration property as integer.
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.warn("Invalid integer value for {}: {}", key, value);
            }
        }
        return defaultValue;
    }

    /**
     * Get configuration property as boolean.
     */
    public boolean getBoolProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");
        }
        return defaultValue;
    }

    /**
     * Set configuration property.
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Save configuration to file.
     */
    public void save() {
        try {
            properties.store(new java.io.FileOutputStream(configPath), "TemporalDB CLI Configuration");
            logger.info("Saved configuration to: {}", configPath);
        } catch (Exception e) {
            logger.error("Could not save configuration: {}", e.getMessage());
        }
    }
}
