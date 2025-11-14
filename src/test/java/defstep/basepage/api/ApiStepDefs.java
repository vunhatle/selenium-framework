package defstep.basepage.api;

import common.api.baseapi.BaseResponse;
import common.api.property.RetryApiProperty;
import common.api.apiutils.JsonProcessing;
import common.api.resttemplate.CommonApiService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiStepDefs {

    private CommonApiService api;
    private int response;
    private HttpHeaders headers = new HttpHeaders();
    private Map<String, String> queryParams = new HashMap<>();

    @Given("Base URL is {string}")
    public void setBaseUrl(String url) { api = new CommonApiService(url); }

    @Given("I set header {string} to {string}")
    public void setHeader(String key, String value) { headers.set(key, value); }

    @Given("I set query params:")
    public void setQueryParams(DataTable table) { queryParams.putAll(table.asMap(String.class, String.class)); }

    @When("I send {word} request to {string}")
    public void sendRequest(String method, String endpoint) throws InterruptedException {
        BaseResponse baseResponse = api.sendWithRetry(method, endpoint, queryParams, null, headers, 3);
        response = baseResponse.getStatusCode();
    }

    @Then("the response status code should be {int}")
    public void checkStatusCode(int expected) {
        Assert.assertEquals(response, expected);
    }

    @When("the response status code is {int} when I send {string} request to {string} with body csv {string}")
    public void theResponseStatusCodeShouldBeWhenISendRequestToWithBodyCsv(int status, String method, String endpoint, String csv) throws InterruptedException {
        List<Map<String, Object>> requestBody = JsonProcessing.readCsvToRequestBodies(csv);
        for (Map<String,Object> eachRequestBody : requestBody) {
            BaseResponse response = api.sendWithRetry(
                    method,
                    endpoint,
                    null,
                    eachRequestBody,
                    new HttpHeaders(),
                    RetryApiProperty.maxRetry
            );
            Assert.assertEquals(response.getStatusCode(), status, "request failed");
        }
    }

}
