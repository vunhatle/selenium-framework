package common.api;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginHelper {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String loginAndGetToken(String username, String password,String url) {
        try {
            String loginBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(loginBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url , HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("access_token").asText();
            } else {
                throw new RuntimeException("Login failed with status code: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage(), e);
        }
    }
}

