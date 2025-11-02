package runner.cucumberrunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import utils.CsvFeatureLoader;

@CucumberOptions(
        features = "src/test/resources/features/",
        glue = {"defstep"},
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    /**
     * Chạy trước toàn bộ suite:
     * - Tạo file _generated.feature cho các feature có tag @csv
     * - Bỏ qua feature không có tag @csv
     */
    @BeforeSuite(alwaysRun = true)
    public void preprocessFeatures() throws Exception {
        CsvFeatureLoader.preprocessAllFeatures();
    }
}

