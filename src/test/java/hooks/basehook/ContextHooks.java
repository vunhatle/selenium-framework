package hooks.basehook;

import org.testng.annotations.AfterClass;
import testdata.GlobalFeatureTestDataContext;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import setup.drivers.DriverUtils;
import testdata.FeatureKeyManager;

public class ContextHooks {

    private static final ThreadLocal<String> currentSuite = new ThreadLocal<>();
    private static final ThreadLocal<String> currentTestCase = new ThreadLocal<>();

    @Before
    public void beforeScenario(Scenario scenario) {
        String suite = DriverUtils.getCurrentSuite(); // tên suite đã set trong @BeforeClass

        // Lấy tên feature từ scenario URI
        String featureName = scenario.getUri().toString(); // ví dụ: "file:/.../login_generated.feature"

        // Tạo key theo suite + featureName
        String featureKey = FeatureKeyManager.getFeatureKey(suite, featureName);

        currentSuite.set(suite);
        currentTestCase.set(featureKey);

        GlobalFeatureTestDataContext.getInstance().init(suite, featureKey);

        System.out.printf("[Before] suite=%s | featureKey=%s%n", suite, featureKey);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        String suite = DriverUtils.getCurrentSuite();

        // Xóa toàn bộ feature của suite này
        GlobalFeatureTestDataContext.getInstance().clearSuite(suite);
        FeatureKeyManager.clearSuite(suite);

        // Xóa toàn bộ data Map của suite này
        GlobalFeatureTestDataContext.getInstance().clearSuiteMap(suite);
        FeatureKeyManager.clearSuite(suite);

        DriverUtils.quitDriver();
    }

    public static String getCurrentSuite() { return currentSuite.get(); }
    public static String getCurrentTestCase() { return currentTestCase.get(); }
}
