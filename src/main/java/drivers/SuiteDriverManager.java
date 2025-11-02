package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuiteDriverManager {

    private static final Logger log = Logger.getLogger(SuiteDriverManager.class.getName());
    private static final ConcurrentHashMap<String, WebDriver> drivers = new ConcurrentHashMap<>();

    static {
        // Giảm bớt verbose log từ Selenium / Netty
        Logger.getLogger("org.openqa.selenium").setLevel(Level.WARNING);
        Logger.getLogger("io.netty").setLevel(Level.WARNING);
        Logger.getLogger("org.asynchttpclient").setLevel(Level.WARNING);
        // Tắt verbose debug log của Apache HttpClient / Selenium
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("org.apache.hc.client5.http").setLevel(Level.WARNING);
        java.util.logging.Logger.getLogger("org.apache.hc.core5.http").setLevel(Level.WARNING);
    }

    /** Lấy driver theo suite + thread */
    public static WebDriver getDriver(String suiteName, String browser) {
        String key = getKey(suiteName);
        return drivers.computeIfAbsent(key, k -> createDriver(browser));
    }

    /** Lấy driver đã tồn tại (nếu chưa khởi tạo sẽ throw) */
    public static WebDriver getDriver(String suiteName) {
        String key = getKey(suiteName);
        WebDriver driver = drivers.get(key);
        if (driver == null) {
            throw new IllegalStateException(
                    "Driver cho suite '" + suiteName + "' trên thread " + Thread.currentThread().getId() + " chưa được khởi tạo!");
        }
        return driver;
    }

    /** Quit driver đúng thread */
    public static void quitDriver(String suiteName) {
        String key = getKey(suiteName);
        WebDriver driver = drivers.remove(key);
        if (driver != null) {
            driver.quit();
            log.info("Driver for suite '" + suiteName + "' quit at " + LocalDateTime.now() + " on thread " + Thread.currentThread().getId());
        }
    }

    /** Tạo key riêng cho mỗi thread */
    private static String getKey(String suiteName) {
        //return suiteName.toLowerCase() + "_" + Thread.currentThread().getId();
        return suiteName.toLowerCase();
    }

    /** Khởi tạo driver */
    private static WebDriver createDriver(String browser) {
        if (browser == null) browser = "chrome";

        log.info(browser.substring(0, 1).toUpperCase() + browser.substring(1)
                + " starts for suite at " + LocalDateTime.now() + " on thread " + Thread.currentThread().getId());

        switch (browser.toLowerCase()) {
            case "chrome":
                //WebDriverManager.chromedriver().setup();
                System.setProperty("webdriver.chrome.driver",
                        "C:\\Users\\Thinkpad T14S G1\\.m2\\webdriver\\chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(false);
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(false);
                WebDriver driver = new FirefoxDriver(firefoxOptions);
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
                return driver;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

}
