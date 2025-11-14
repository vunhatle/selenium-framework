package defstep.basepage.ui;

import hooks.basehook.ScenarioHooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;

public class LoginSteps {

    @Given("I load data file {string}")
    public void i_load_data_file(String csvFile) {
        String env = System.getProperty("env", "stg"); // default env
        String filePath = String.format("src/test/resources/dataprofiles/%s/%s", env, csvFile);
        List<Map<String, String>> data = ScenarioHooks.loadCsv(filePath);
        ScenarioHooks.setScenarioData(data);
        System.out.println("✅ Loaded data file: " + filePath + " (" + data.size() + " rows)");
    }

    @When("I login with each username and password")
    public void i_login_with_each_username_and_password() {
        List<Map<String, String>> data = ScenarioHooks.getScenarioData();
        if (data == null) throw new RuntimeException("CSV data not loaded!");

        for (Map<String, String> row : data) {
            String username = row.get("username");
            String password = row.get("password");
            String message = row.get("message");

            System.out.println("Login test => " + username + " / " + password + " / " + message);

            // TODO: Gọi hàm login(username, password)
            // TODO: Verify message hiển thị == message
        }
    }

    @Given("User opens login page")
    public void openLoginPage() {
        System.out.println("Open login page");
    }

    @When("User login with {string} and {string}")
    public void userLoginWith(String username, String password) {
        System.out.println("Login with " + username + " / " + password);
    }

    @Then("Message {string} should be displayed")
    public void messageShouldBeDisplayed(String message) {
        System.out.println("Verify message: " + message);
    }
}
