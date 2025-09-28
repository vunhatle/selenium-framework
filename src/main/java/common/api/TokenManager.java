package common.api;

import java.util.HashMap;
import java.util.Map;

public class TokenManager {

    private static final Map<String, String> tokenCache = new HashMap<>();

    private static String buildCacheKey(String username, String url) {
        return username + "@" + url;
    }

    public static String getTokenForUser(String username, String password, String url) {
        String cacheKey = buildCacheKey(username, url);
        if (!tokenCache.containsKey(cacheKey)) {
            String token = LoginHelper.loginAndGetToken(username, password, url);
            tokenCache.put(cacheKey, token);
        }
        return tokenCache.get(cacheKey);
    }

    public static void refreshTokenForUser(String username, String password, String url) {
        String cacheKey = buildCacheKey(username, url);
        String token = LoginHelper.loginAndGetToken(username, password, url);
        tokenCache.put(cacheKey, token);
    }

    public static void clearAllTokens() {
        tokenCache.clear();
    }
}
