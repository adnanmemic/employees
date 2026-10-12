package com.company.employees;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.security.user.name=test-user",
        "spring.security.user.password=test-password"
})
class EmployeesApplicationTests {
    @Autowired
    private Environment environment;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5)).build();

    @Test
    void applicationEndpointsReturnExpectedResponses() throws Exception {
        assertResponse("/employee", "employee!!!");
        assertResponse("/greet", "whaaaat");
        assertResponse("/security", environment.getRequiredProperty("security.name"));
    }

    @Test
    void applicationRequiresAuthentication() throws Exception {
        assertEquals(401, request("/employee", false).statusCode());
    }

    @Test
    void healthAndInfoAreAvailableButEnvironmentIsNotExposed() throws Exception {
        HttpResponse<String> health = request("/actuator/health", true);
        assertEquals(200, health.statusCode());
        assertTrue(health.body().contains("\"status\":\"UP\""));
        HttpResponse<String> info = request("/actuator/info", true);
        assertEquals(200, info.statusCode());
        assertTrue(info.body().contains("My Super Cool App"));
        assertEquals(404, request("/actuator/env", true).statusCode());
    }

    private void assertResponse(String path, String expected) throws Exception {
        HttpResponse<String> response = request(path, true);
        assertEquals(200, response.statusCode());
        assertEquals(expected, response.body());
    }

    private HttpResponse<String> request(String path, boolean authenticated) throws Exception {
        String port = environment.getRequiredProperty("local.server.port");
        HttpRequest.Builder builder = HttpRequest.newBuilder(
                URI.create("http://127.0.0.1:" + port + "/app" + path))
                .timeout(Duration.ofSeconds(5));
        if (authenticated) {
            String credentials = Base64.getEncoder().encodeToString(
                    "test-user:test-password".getBytes(StandardCharsets.UTF_8));
            builder.header("Authorization", "Basic " + credentials);
        }
        return client.send(builder.GET().build(), HttpResponse.BodyHandlers.ofString());
    }
}
