package hooks.suitelistener;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.DriverUtils;

public class SuiteListener implements ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        String suiteName = suite.getName();
        String browser = suite.getXmlSuite().getTests().get(0).getLocalParameters().get("browser");
        DriverUtils.setCurrentSuite(suiteName);
        DriverUtils.getDriver(browser);
        System.out.println(">>> SuiteListener setCurrentSuite: " + suiteName + " with browser: " + browser);
    }

    @Override
    public void onFinish(ISuite suite) {
        DriverUtils.quitDriver();
    }

}