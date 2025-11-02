package hooks.googlehook;

import config.EnvironmentConfig;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.ITestContext;
import utils.DriverUtils;
import utils.I18nHelper;


public class TestSuiteHooks {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) throws Exception {
        // Get ENV and LANG
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        System.setProperty("maven.surefire.debug", "true");
        String env = System.getProperty("env", "dev");
        String lang = System.getProperty("language", "en");
        System.out.println("=== Running on ENV: " + env + " | LANG: " + lang + " ===");
        EnvironmentConfig.load(env);
        I18nHelper.load(lang);

        // Get driver for each suite
        WebDriver driver = DriverUtils.getDriver();
        driver.get("https://www.google.com");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

    }
}

