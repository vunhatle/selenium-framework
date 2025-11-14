package hooks.basehook;

import setup.config.EnvironmentConfig;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.ITestContext;
import testdata.GlobalSuiteTestDataContext;
import setup.drivers.DriverUtils;
import utils.i18n.I18nHelper;


public class TestSuiteHooks {
    //private static final ThreadLocal<String> currentSuite = new ThreadLocal<>();

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

        // Lấy suite name từ ITestContext (ổn định hơn DriverUtils)
        String suite = System.getProperty("currentSuite");
        //currentSuite.set(suite);
        GlobalSuiteTestDataContext.getInstance().init(suite);


        // Get driver for each suite
        WebDriver driver = DriverUtils.getDriver();
        driver.get("https://www.google.com");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        String suite = DriverUtils.getCurrentSuite();
        // Xóa toàn bộ data của suite này
        GlobalSuiteTestDataContext.getInstance().clearSuite(suite);

    }
    public static String getCurrentSuite() { return System.getProperty("currentSuite"); }
}

