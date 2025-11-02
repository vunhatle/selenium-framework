package hooks;

import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import utils.DriverUtils;

public class BaseHooks {
    private static boolean initialized = false;

    @Before(order = 0)
    public void beforeAll() {
        if (!initialized) {
            System.out.println(">>> Running BaseHooks.beforeAll()");
            WebDriver driver = DriverUtils.getDriver();
            driver.get("https://www.google.com");
            initialized = true;
        }
    }
}
