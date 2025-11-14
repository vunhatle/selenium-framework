package setup.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentConfig {
    private static Properties properties = new Properties();

    public static void load(String env) {
        try (InputStream input = EnvironmentConfig.class
                .getClassLoader()
                .getResourceAsStream("envfiles/" + env + ".properties")) {
            if (input == null) {
                throw new RuntimeException("Config file not found for env: " + env);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load env config: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}