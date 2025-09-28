package hooks.googlehook;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.ITestContext;
import utils.DriverUtils;


public class TestSuiteHooks {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        WebDriver driver = DriverUtils.getDriver(); // lấy driver đã init từ SuiteListener
        driver.get("https://www.google.com");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

    }
}

