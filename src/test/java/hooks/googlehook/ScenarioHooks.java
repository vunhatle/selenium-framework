package hooks.googlehook;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.CsvUtils;

import java.util.*;

public class ScenarioHooks {
    private static final ThreadLocal<List<Map<String, String>>> scenarioData = new ThreadLocal<>();

    public static List<Map<String, String>> loadCsv(String filePath) {
        return CsvUtils.readCsv(filePath);
    }

    public static void setScenarioData(List<Map<String, String>> data) {
        scenarioData.set(data);
    }

    public static List<Map<String, String>> getScenarioData() {
        return scenarioData.get();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("=== Start Scenario: " + scenario.getName() + " ===");
        // Reset data trước mỗi scenario
        scenarioData.remove();
    }
}
