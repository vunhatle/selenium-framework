    package setup.drivers;

    import org.openqa.selenium.WebDriver;

    public class DriverFactory {
        // ThreadLocal đảm bảo mỗi thread có 1 instance riêng
        private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

        public static WebDriver getDriver() {
            return driver.get();
        }

        public static void setDriver(WebDriver webDriver) {
            driver.set(webDriver);
        }

        public static void removeDriver() {
            driver.get().quit();
            driver.remove();
        }
    }
