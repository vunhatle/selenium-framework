package common.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class CommonApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;

    public CommonApiService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public BaseResponse sendRequest(ApiMethod method, String endpoint, Map<String, String> queryParams, Map<String, ?> body, HttpHeaders headers) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + endpoint);
            if (queryParams != null) queryParams.forEach(builder::queryParam);

            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = (body != null) ? new HttpEntity<>(body, headers) : new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.valueOf(method.name()),
                    entity,
                    String.class
            );

            JsonNode data = mapper.readTree(responseEntity.getBody());
            return new BaseResponse(responseEntity.getStatusCodeValue(), "Success", data);

        } catch (Exception e) {
            return new BaseResponse(500, "Exception: " + e.getMessage(), null);
        }
    }

    @Step("Send {method} {endpoint} with body {body} and params {queryParams}")
    public BaseResponse sendRequest(String method, String endpoint, Map<String, String> queryParams, Map<String, ?> body, HttpHeaders headers) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + endpoint);
            if (queryParams != null) queryParams.forEach(builder::queryParam);

            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = (body != null) ? new HttpEntity<>(body, headers) : new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.valueOf(method),
                    entity,
                    String.class
            );

            JsonNode data = mapper.readTree(responseEntity.getBody());
            return new BaseResponse(responseEntity.getStatusCodeValue(), "Success", data);

        } catch (Exception e) {
            return new BaseResponse(500, "Exception: " + e.getMessage(), null);
        }
    }

    public BaseResponse sendWithRetry(ApiMethod method, String endpoint, Map<String, String> queryParams, Map<String, ?> body, HttpHeaders headers, int maxRetry) throws InterruptedException {
        int attempt = 0;
        BaseResponse response;

        do {
            response = sendRequest(method, endpoint, queryParams, body, headers);
            System.out.println("Attempt: " + (attempt + 1));
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Message: " + response.getMessage());
            System.out.println("Data: " + response.getData());
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                return response;
            }
            attempt++;
            Thread.sleep(1000);
        } while (attempt < maxRetry);

        return response;
    }

    @Step("Send {method} {endpoint} with retry logic. Max attempts: {maxRetry}")
    public BaseResponse sendWithRetry(String method, String endpoint, Map<String, String> queryParams, Map<String, ?> body, HttpHeaders headers, int maxRetry) throws InterruptedException {
        int attempt = 0;
        BaseResponse response;

        do {
            response = sendRequest(method, endpoint, queryParams, body, headers);
            System.out.println("Attempt: " + (attempt + 1));
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Message: " + response.getMessage());
            System.out.println("Data: " + response.getData());
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                return response;
            }
            attempt++;
            Thread.sleep(1000);
        } while (attempt < maxRetry);

        return response;
    }


}
