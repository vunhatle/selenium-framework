package hooks.suitelistener;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.DriverUtils;

public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        String suiteName = suite.getName();
        String browser = "chrome"; // default

        try {
            if (!suite.getXmlSuite().getTests().isEmpty()) {
                var test = suite.getXmlSuite().getTests().get(0);
                var params = test.getLocalParameters();
                if (params.containsKey("browser")) {
                    browser = params.get("browser");
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Không lấy được browser từ suite XML: " + e.getMessage());
        }

        // 👉 Gán suiteName cho ThreadLocal
        DriverUtils.setCurrentSuite(suiteName);

        // 👉 Gán thêm cho System property để thread khác (như Cucumber) có thể đọc được
        System.setProperty("currentSuite", suiteName);
        // ✅ Tạo driver sớm cho suite này
        DriverUtils.getDriver(browser);
        System.out.println(">>> SuiteListener setCurrentSuite: " + suiteName + " with browser: " + browser);
    }

    @Override
    public void onFinish(ISuite suite) {
        DriverUtils.quitDriver();
        System.out.println(">>> SuiteListener finished: " + suite.getName());
    }
}
