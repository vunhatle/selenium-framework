package testng;

import com.fasterxml.jackson.databind.JsonNode;
import common.api.CommonBaseApi;
import common.url.CommonBaseUrl;
import common.api.ApiMethod;
import common.api.BaseResponse;
import common.api.CommonApiService;
import common.api.CommonJsonProcessing;
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
        api = new CommonApiService(CommonBaseUrl.BASE_URL);
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
                CommonBaseApi.maxRetry
        );
        CommonJsonProcessing.writeDataToCsv((JsonNode) response.getData(),"api_get_output.csv");
        Assert.assertEquals(response.getStatusCode(), 200, "GET request failed");
    }

    @Test
    public void testPostRequest() throws InterruptedException {
        List<Map<String, Object>> requestBody = CommonJsonProcessing.readCsvToRequestBodies("postCsv.csv");
        for (Map<String,Object> eachRequestBody : requestBody) {
            System.out.println("Post request is sending: ");
            BaseResponse response = api.sendWithRetry(
                    ApiMethod.POST,
                    "/posts",
                    null,
                    eachRequestBody,
                    new HttpHeaders(),
                    CommonBaseApi.maxRetry
            );
            Assert.assertEquals(response.getStatusCode(), 201, "POST request failed");
        }
    }

    @Test
    public void testPutRequest() throws InterruptedException {
        List<Map<String, Object>> requestBody = CommonJsonProcessing.readCsvToRequestBodies("updateCSV.csv");
        for (Map<String,Object> eachRequestBody : requestBody) {
            BaseResponse response = api.sendWithRetry(
                    ApiMethod.PUT,
                    "/posts/1",
                    null,
                    eachRequestBody,
                    new HttpHeaders(),
                    CommonBaseApi.maxRetry
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
                CommonBaseApi.maxRetry
        );

        // jsonplaceholder sẽ trả 200 hoặc 204 tùy API, nên test soft 1 chút
        Assert.assertTrue(
                response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "DELETE request failed"
        );
    }
}
