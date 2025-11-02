package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Map;

public class I18nHelper {
    private static Map<String, String> messages;

    public static void load(String lang) {
        try (InputStream input = I18nHelper.class
                .getClassLoader()
                .getResourceAsStream("i18n/" + lang + ".json")) {
            if (input == null) {
                throw new RuntimeException("Language file not found: " + lang);
            }
            ObjectMapper mapper = new ObjectMapper();
            messages = mapper.readValue(input, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load i18n: " + e.getMessage(), e);
        }
    }

    public static String t(String key) {
        return messages.getOrDefault(key, key);
    }
}
