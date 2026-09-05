package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads key/value pairs from src/test/resources/config.properties.
 * Centralizes environment data (URLs, credentials, timeouts) so no test
 * or page class ever hardcodes environment-specific values.
 */
public class ConfigReader {

    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    private static Properties properties;

    private ConfigReader() {
        // utility class - no instantiation
    }

    private static void loadPropertiesIfNeeded() {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load config.properties from path: " + CONFIG_PATH, e);
            }
        }
    }

    public static String get(String key) {
        loadPropertiesIfNeeded();
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing key '" + key + "' in config.properties");
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
