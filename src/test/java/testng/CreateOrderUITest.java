package testng;

import common.api.ApiHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CreateOrderUITest {
    @Test
    public void testCallApiAsSuperUser() {
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        HttpMethod method = HttpMethod.GET;
        Object body = null;
        ResponseEntity<String> response = ApiHelper.callApiAsSuperUser(url, method, body);
        assertEquals(response.getStatusCodeValue(), 200, "Status code phải là 200 OK");
        assertTrue(response.getBody().contains("\"id\": 1"), "Response phải chứa id 1");
    }

}
