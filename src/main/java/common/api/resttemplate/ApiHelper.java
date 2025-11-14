package common.api.resttemplate;
import setup.config.ConfigManager;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Data
@Log4j2
public class ApiHelper {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String SUPER_USER_TOKEN = ConfigManager.getProperty("super.user.token");
    private static final String USER_TOKEN_A = ConfigManager.getProperty("user.a.token");

    // get super user token
    public static ResponseEntity<String> callApiAsSuperUser(String url, HttpMethod method, Object body) {
        log.info("Super user token: "+ SUPER_USER_TOKEN);
        return callApi(url, method, body, SUPER_USER_TOKEN);
    }

    // get user token
    public static ResponseEntity<String> callApiAsUserA(String url, HttpMethod method, Object body) {
        return callApi(url, method, body, USER_TOKEN_A);
    }

    // common callAPI for 2 above methods
    public static ResponseEntity<String> callApi(String url, HttpMethod method, Object body,String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, method, entity, String.class);
    }
}
