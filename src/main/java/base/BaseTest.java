package base;

import config.EnvironmentConfig;

public class BaseTest {
    protected String getBaseUrl() {
        return EnvironmentConfig.get("server.baseUrl");
    }
    protected String getUserName() {
        return EnvironmentConfig.get("account.username");
    }
    protected String getAccountPassword() {
        return EnvironmentConfig.get("account.password");
    }

}
