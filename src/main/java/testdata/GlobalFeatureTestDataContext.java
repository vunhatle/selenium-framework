package testdata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quản lý dữ liệu test automation theo suite và featureKey
 */
public class GlobalFeatureTestDataContext {

    // Map lưu data theo suite -> featureKey -> key-value data
    private final Map<String, Map<String, Map<String, Object>>> dataStore = new ConcurrentHashMap<>();
    private final Map<String,Map<String,Map<String,Map<String,Object>>>> dataStoreMap = new ConcurrentHashMap<>();
    // Singleton instance
    private static final GlobalFeatureTestDataContext INSTANCE = new GlobalFeatureTestDataContext();

    private GlobalFeatureTestDataContext() {}

    public static GlobalFeatureTestDataContext getInstance() {
        return INSTANCE;
    }

    /**
     * Khởi tạo data map cho suite + featureKey
     */
    public void init(String suite, String featureKey) {
        dataStore.computeIfAbsent(suite, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(featureKey, k -> new ConcurrentHashMap<>());
        dataStoreMap.computeIfAbsent(suite, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(featureKey, k -> new ConcurrentHashMap<>());
    }

    /**
     * Lấy map data cho suite + featureKey
     */
    public Object getData(String suite, String featureKey,String key) {
        return dataStore.getOrDefault(suite, Map.of())
                .getOrDefault(featureKey, Map.of()).get(key);
    }

    public Map<String,Object> getDataMap(String suite, String featureKey,String key) {
        return dataStoreMap.getOrDefault(suite, Map.of())
                .getOrDefault(featureKey, Map.of()).get(key);
    }

    /**
     * Lưu giá trị vào suite + featureKey
     */
    public void putData(String suite, String featureKey, String key, Object value) {
        dataStore.computeIfAbsent(suite, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(featureKey, k -> new ConcurrentHashMap<>())
                .put(key, value);
    }

    public void putDataMap(String suite, String featureKey, String key, Map<String,Object> value) {
        dataStore.computeIfAbsent(suite, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(featureKey, k -> new ConcurrentHashMap<>())
                .put(key, value);
    }

    /**
     * Xóa toàn bộ dữ liệu của suite (sau @AfterClass)
     */
    public void clearSuite(String suite) {
        dataStore.remove(suite);
    }

    public void clearSuiteMap(String suite) {
        dataStoreMap.remove(suite);
    }

    public void clearFeature(String suite, String featureKey) {
        Map<String, Map<String, Object>> suiteMap = dataStore.get(suite);
        if (suiteMap != null) {
            suiteMap.remove(featureKey);
        }
    }

    public void clearFeatureMap(String suite, String featureKey) {
        Map<String,Map<String,Map<String,Object>>> suiteMap = dataStoreMap.get(suite);
        if (suiteMap != null) {
            suiteMap.remove(featureKey);
        }
    }

    /**
     * Xóa toàn bộ dữ liệu
     */
    public void clearAll() {
        dataStore.clear();
    }

    public void clearAllMap() {
        dataStoreMap.clear();
    }


}