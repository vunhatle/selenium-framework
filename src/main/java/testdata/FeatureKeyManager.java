package testdata;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FeatureKeyManager {

    // Map lưu featureKey theo suite + featureName
    private static final Map<String, Map<String, String>> suiteFeatureMap = new ConcurrentHashMap<>();

    public static String getFeatureKey(String suite, String featureName) {
        if (suite == null || suite.isEmpty()) {
            throw new IllegalArgumentException("Suite name không được null hoặc empty");
        }

        if (featureName == null || featureName.isEmpty()) {
            featureName = "defaultFeature";
        }

        return suiteFeatureMap
                .computeIfAbsent(suite, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(featureName, f ->
                        "feature-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8)
                );
    }

    public static void clearSuite(String suite) {
        suiteFeatureMap.remove(suite);
    }

    public static void clearAll() {
        suiteFeatureMap.clear();
    }
}
