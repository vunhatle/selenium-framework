package runner.loginrunner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import utils.CsvFeatureLoader;

@CucumberOptions(
        features = "src/test/resources/features/loginfeature",
        glue = {"defstep","hooks.googlehook"},
        plugin = {"pretty", "html:target/cucumber-report.html"},
        tags = ""

)
public class LoginRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite(alwaysRun = true)
    public void generateAllFeatures() throws Exception {
        CsvFeatureLoader.preprocessAllFeatures();
    }
}