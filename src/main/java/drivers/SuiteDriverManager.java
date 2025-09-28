package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class SuiteDriverManager {

    private static final ConcurrentHashMap<String, WebDriver> drivers = new ConcurrentHashMap<>();

    public static WebDriver getDriver(String suiteName, String browser) {
        return drivers.computeIfAbsent(suiteName, k -> createDriver(browser));
    }

    public static WebDriver getDriver(String suiteName) {
        WebDriver driver = drivers.get(suiteName);
        if (driver == null) {
            throw new IllegalStateException("Driver cho suite " + suiteName + " chưa được khởi tạo!");
        }
        return driver;
    }

    public static void quitDriver(String suiteName) {
        WebDriver driver = drivers.remove(suiteName);
        if (driver != null) {
            driver.quit();
        }
    }

    private static WebDriver createDriver(String browser) {
        if (browser == null) browser = "chrome";
        switch (browser.toLowerCase()) {
            case "chrome":
                System.out.println("Chrome starts: "+ LocalDateTime.now());
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                System.out.println("Firefox starts: "+ LocalDateTime.now());
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                return new FirefoxDriver(firefoxOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}
