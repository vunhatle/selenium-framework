package testdata;

import lombok.Getter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalTestDataContext {
    @Getter
    private static final GlobalTestDataContext instance = new GlobalTestDataContext();

    // Structure: profile -> suite -> testcase -> key -> value
    private final Map<String, Map<String, Map<String, Map<String, Object>>>> data = new ConcurrentHashMap<>();

    private GlobalTestDataContext() {}

    public void init(String profile, String suite, String testcase) {
        data.computeIfAbsent(profile, p -> new ConcurrentHashMap<>())
                .computeIfAbsent(suite, s -> new ConcurrentHashMap<>())
                .computeIfAbsent(testcase, t -> new ConcurrentHashMap<>());
    }

    public void put(String profile, String suite, String testcase, String key, Object value) {
        init(profile, suite, testcase);
        data.get(profile).get(suite).get(testcase).put(key, value);
    }

    public Object get(String profile, String suite, String testcase, String key) {
        return data.getOrDefault(profile, Map.of())
                .getOrDefault(suite, Map.of())
                .getOrDefault(testcase, Map.of())
                .get(key);
    }

    public void clearTestCase(String profile, String suite, String testcase) {
        Map<String, Map<String, Map<String, Object>>> suiteMap = data.get(profile);
        if (suiteMap != null) {
            Map<String, Map<String, Object>> testMap = suiteMap.get(suite);
            if (testMap != null) testMap.remove(testcase);
        }
    }

    public void clearSuite(String profile, String suite) {
        Map<String, Map<String, Map<String, Object>>> suiteMap = data.get(profile);
        if (suiteMap != null) suiteMap.remove(suite);
    }

    public void clearProfile(String profile) {
        data.remove(profile);
    }

    public void clearAll() {
        data.clear();
    }
}
