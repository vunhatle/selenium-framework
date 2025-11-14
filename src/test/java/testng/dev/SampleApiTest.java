package testng.dev;

import com.fasterxml.jackson.databind.JsonNode;
import common.api.property.RetryApiProperty;
import common.urlnotused.CommonBaseUrl;
import common.api.property.ApiMethod;
import common.api.baseapi.BaseResponse;
import common.api.resttemplate.CommonApiService;
import common.api.apiutils.JsonProcessing;
import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleApiTest {

    private CommonApiService api;

    @BeforeClass
    public void setup() {
        api = new CommonApiService("https://jsonplaceholder.typicode.com");
    }

    @Test
    public void testGetRequest() throws InterruptedException, IOException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("postId", "1");

        BaseResponse response = api.sendWithRetry(
                ApiMethod.GET,
                "/comments",
                queryParams,
                null,
                new HttpHeaders(),
                RetryApiProperty.maxRetry
        );
        JsonProcessing.writeDataToCsv((JsonNode) response.getData(),"api_get_output.csv");
        Assert.assertEquals(response.getStatusCode(), 200, "GET request failed");
    }

    @Test
    public void testPostRequest() throws InterruptedException {
        List<Map<String, Object>> requestBody = JsonProcessing.readCsvToRequestBodies("postCsv.csv");
        for (Map<String,Object> eachRequestBody : requestBody) {
            System.out.println("Post request is sending: ");
            BaseResponse response = api.sendWithRetry(
                    ApiMethod.POST,
                    "/posts",
                    null,
                    eachRequestBody,
                    new HttpHeaders(),
                    RetryApiProperty.maxRetry
            );
            Assert.assertEquals(response.getStatusCode(), 201, "POST request failed");
        }
    }

    @Test
    public void testPutRequest() throws InterruptedException {
        List<Map<String, Object>> requestBody = JsonProcessing.readCsvToRequestBodies("updateCSV.csv");
        for (Map<String,Object> eachRequestBody : requestBody) {
            BaseResponse response = api.sendWithRetry(
                    ApiMethod.PUT,
                    "/posts/1",
                    null,
                    eachRequestBody,
                    new HttpHeaders(),
                    RetryApiProperty.maxRetry
            );

            Assert.assertEquals(response.getStatusCode(), 200, "PUT request failed");
        }
    }

    @Test
    public void testDeleteRequest() throws InterruptedException {
        BaseResponse response = api.sendWithRetry(
                ApiMethod.DELETE,
                "/posts/1",
                null,
                null,
                new HttpHeaders(),
                RetryApiProperty.maxRetry
        );
        Assert.assertTrue(
                response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "DELETE request failed"
        );
    }
}
