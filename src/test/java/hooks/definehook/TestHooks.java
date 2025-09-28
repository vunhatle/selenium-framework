/*
package hooks;
import config.ConfigManager;
import dbmanager.DbConnector;
import dbmanager.DbSshConfig;
import drivers.DriverFactory;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import utils.DBUtils;


public class TestHooks {
    private static DbSshConfig cfg;
    private static DbConnector connector;
    */
/*
        Cucumber annotations
        run before/after each scenario, can tag on it @Before("@login") using or/and/not
        to clean up or close resource of each scenario
    *//*

    @Before
    public void setUp() {
        WebDriver webDriver;
        String browser = System.getProperty("browser", "chrome");
        switch (browser.toLowerCase()) {
            case "firefox":
                webDriver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                webDriver = new ChromeDriver();
                break;
        }
        DriverFactory.setDriver(webDriver);
    }

    @After
    public void tearDown() {
        DriverFactory.removeDriver();
    }

    */
/*
        run before/after each step, can tag on it @BeforeStep("@login") using or/and/not
        to log each step of scenarios
        to capture screenshot after each step
    *//*


    @BeforeStep
    public void beforeEachStep() {

    }

    @AfterStep
    public void afterEachStep() {

    }

    */
/*
        run before/after all scenarios for each runner, can tag on it @BeforeAll("@login") using or/and/not
        need to declare static
        to connect/disconnect DB
        to load config
        to start/stop server
    *//*


    @BeforeClass
    public static void beforeAllScenarios() {

    }

    @AfterClass
    public static void afterAllScenarios() {

    }

    // BeforeSuite to run before/after a suite, ket noi den test_sigma
    @BeforeSuite
    public void beforeSuite() {
        cfg = new DbSshConfig();
        cfg.dbJdbcPrefix = "jdbc:mysql://";
        cfg.dbHost = "127.0.0.1";
        cfg.dbPort = 3306;
        cfg.dbName = "testsigma_opensource";
        cfg.dbUser = "root";
        cfg.dbPassword = "root123";
        // Nếu không dùng SSH
        cfg.sshHost = null;
        cfg.maximumPoolSize = 10;

        connector = new DbConnector(cfg);
        DBUtils.runWithDb(cfg, connector);

    }

    @AfterSuite
    public void afterSuite() {
        DBUtils.stopWithDb(connector);
    }




}
*/
