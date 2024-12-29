package tests.AuthenticationAPIs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class LogoutApiTest {

    private String authToken;

    @BeforeClass
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ValidEmailandPassword");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");  // Adjust "token" based on your JSON structure
    }

    @Test(priority = 1)
    public void logoutSuccessTest() {
        // Call the logout API using the token from login
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/auth/logout");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/auth/logout");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!"); // Adjust based on expected status code

        Assert.assertNotNull(response.jsonPath().getString("message"), "Successfully signed out");
    }

    @Test(priority = 2)
    public void logoutWithoutTokenTest() {
        // Call the logout API using the token from login
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/api/auth/logout");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/auth/logout");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!"); // Adjust based on expected status code

        Assert.assertNotNull(response.jsonPath().getString("message"), "Unauthorized");
    }

    @Test(priority = 3)
    public void logoutWithInvalidTokenTest() {
        // Call the logout API using the token from login
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + "InvalidToken")
                .contentType(ContentType.JSON)
                .post("/api/auth/logout");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/auth/logout");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!"); // Adjust based on expected status code

        Assert.assertNotNull(response.jsonPath().getString("message"), "Unauthorized");
    }
}

