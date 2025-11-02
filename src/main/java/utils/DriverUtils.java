package utils;

import drivers.SuiteDriverManager;
import org.openqa.selenium.WebDriver;

public class DriverUtils {

    private static final ThreadLocal<String> currentSuite = ThreadLocal.withInitial(() -> null);

    public static void setCurrentSuite(String suiteName) {
        if (suiteName == null || suiteName.isEmpty()) {
            throw new IllegalArgumentException("Suite name không được null hoặc empty");
        }
        currentSuite.set(suiteName);
    }

    public static String getCurrentSuite() {
        return currentSuite.get();
    }

    public static WebDriver getDriver() {
        String suiteName = currentSuite.get();
        if (suiteName == null) {
            suiteName = System.getProperty("currentSuite");
            if (suiteName == null) {
                throw new IllegalStateException("Suite name chưa được set! Hãy gọi setCurrentSuite() trước.");
            }
            currentSuite.set(suiteName);
        }
        return SuiteDriverManager.getDriver(suiteName);
    }

    public static WebDriver getDriver(String browser) {
        String suiteName = currentSuite.get();
        if (suiteName == null) {
            suiteName = System.getProperty("currentSuite");
            if (suiteName == null) {
                throw new IllegalStateException("Suite name chưa được set! Hãy gọi setCurrentSuite() trước.");
            }
            currentSuite.set(suiteName);
        }
        return SuiteDriverManager.getDriver(suiteName, browser);
    }

    public static void quitDriver() {
        String suiteName = currentSuite.get();
        if (suiteName != null) {
            SuiteDriverManager.quitDriver(suiteName);
            currentSuite.remove();
        }
    }
}
