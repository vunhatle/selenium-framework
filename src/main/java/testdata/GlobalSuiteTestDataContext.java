package testdata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quản lý dữ liệu test automation theo suite và featureKey
 */
public class GlobalSuiteTestDataContext {

    // Map lưu data theo suite -> featureKey -> key-value data
    private final Map<String, Map<String, Object>> dataSuiteStore = new ConcurrentHashMap<>();
    private final Map<String,Map<String,Map<String,Object>>> dataSuiteStoreMap = new ConcurrentHashMap<>();
    // Singleton instance
    private static final GlobalSuiteTestDataContext INSTANCE = new GlobalSuiteTestDataContext();

    private GlobalSuiteTestDataContext() {}

    public static GlobalSuiteTestDataContext getInstance() {
        return INSTANCE;
    }

    /**
     * Khởi tạo data map cho suite + featureKey
     */
    public void init(String suite) {
        dataSuiteStore.computeIfAbsent(suite, k -> new ConcurrentHashMap<>());
        dataSuiteStoreMap.computeIfAbsent(suite, k -> new ConcurrentHashMap<>());
    }

    /**
     * Lấy map data cho suite + featureKey
     */
    public Object getData(String suite,String key) {
        return dataSuiteStore.getOrDefault(suite, Map.of()).get(key);
    }

    public Map<String,Object> getDataMap(String suite,String key) {
        return dataSuiteStoreMap.getOrDefault(suite, Map.of()).get(key);
    }

    /**
     * Lưu giá trị vào suite + featureKey
     */
    public void putData(String suite, String key, Object value) {
        dataSuiteStore.computeIfAbsent(suite, k -> new ConcurrentHashMap<>())
                .put(key, value);
    }

    public void putDataMap(String suite, String key, Map<String,Object> value) {
        dataSuiteStore.computeIfAbsent(suite, k -> new ConcurrentHashMap<>())
                .put(key, value);
    }

    /**
     * Xóa toàn bộ dữ liệu của suite (sau @AfterClass)
     */
    public void clearSuite(String suite) {
        dataSuiteStore.remove(suite);
        dataSuiteStoreMap.remove(suite);
    }

    /**
     * Xóa toàn bộ dữ liệu
     */
    public void clearAll() {
        dataSuiteStore.clear();
        dataSuiteStoreMap.clear();
    }

}