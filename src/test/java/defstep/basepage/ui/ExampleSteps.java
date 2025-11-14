package defstep.basepage.ui;

import hooks.basehook.TestSuiteHooks;
import testdata.GlobalFeatureTestDataContext;
import hooks.basehook.ContextHooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import testdata.GlobalSuiteTestDataContext;

public class ExampleSteps {

    @Given("I save data {string} with value {string}")
    public void saveData(String key, String value) {
        GlobalFeatureTestDataContext.getInstance().putData(
                ContextHooks.getCurrentSuite(),
                ContextHooks.getCurrentTestCase(),
                key,
                value
        );
    }

    @Given("I save suite data {string} with value {string}")
    public void saveDataOnSuite(String key, String value) {
        GlobalSuiteTestDataContext.getInstance().putData(
                TestSuiteHooks.getCurrentSuite(),
                key,
                value
        );
    }

    @Then("I print data {string}")
    public void printData(String key) {
        Object val = GlobalFeatureTestDataContext.getInstance().getData(
                ContextHooks.getCurrentSuite(),
                ContextHooks.getCurrentTestCase(),
                key
        );
        System.out.println(
                "[ The value of the key: " +
                        "[" + ContextHooks.getCurrentSuite() + "] " +
                        "[" + ContextHooks.getCurrentTestCase() + "] " +
                        key + " = " + val
        );
    }

    @Then("I print suite data {string}")
    public void printDataOnSuite(String key) {
        Object val = GlobalSuiteTestDataContext.getInstance().getData(
                TestSuiteHooks.getCurrentSuite(),
                key
        );
        System.out.println(
                "[ The suite value of the key: " +
                        "[" + TestSuiteHooks.getCurrentSuite() + "] " +
                        key + " = " + val
        );
    }




}

