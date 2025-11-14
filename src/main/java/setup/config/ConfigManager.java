package setup.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("Không tìm thấy file application.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file config", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return (value != null) ? Integer.parseInt(value) : defaultValue;
    }
}
