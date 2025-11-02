package runner.googlerunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;

@CucumberOptions(
        features = "src/test/resources/features/googlefeature/Google.feature",
        glue = {"defstep","hooks.googlehook"},
        plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
)
@Listeners({hooks.suitelistener.SuiteListener.class})
public class GoogleRunner extends AbstractTestNGCucumberTests {}

