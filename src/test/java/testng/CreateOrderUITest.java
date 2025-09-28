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
        // Arrange
        String url = "https://jsonplaceholder.typicode.com/posts/1"; // Mock API public
        HttpMethod method = HttpMethod.GET;
        Object body = null; // GET không cần body

        // Act
        ResponseEntity<String> response = ApiHelper.callApiAsSuperUser(url, method, body);

        // Assert
        assertEquals(response.getStatusCodeValue(), 200, "Status code phải là 200 OK");
        assertTrue(response.getBody().contains("\"id\": 1"), "Response phải chứa id 1");
    }

}
