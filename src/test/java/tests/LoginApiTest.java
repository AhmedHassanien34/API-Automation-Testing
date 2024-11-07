package tests; // Adjust the package name as per your project structure

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginApiTest {

    @Test(priority = 1)
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ValidEmailandPassword");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("firstName"), "Ahmed", "First name mismatch!");
        Assert.assertEquals(response.jsonPath().getString("email"), "hassaniengivers+2@gmail.com", "Email mismatch!");
        Assert.assertTrue(response.jsonPath().getBoolean("isEmailVerified"), "isEmailVerified should be True!");
        Assert.assertTrue(response.jsonPath().getBoolean("isAccountActive"),"isAccountActive should be True!");

        // Check that accessToken and refreshToken are not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("refreshToken"), "Refresh token should not be null!");
    }

    @Test(priority = 1)
    public void MissingEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("MissingEmailandValidPasswordLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "Email is required");
    }

    @Test(priority = 1)
    public void InvalidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ValidEmailandInvalidPasswordLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "Email or password is incorrect");
    }

    @Test(priority = 1)
    public void UserIsnotExistLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("UserIsnotExistLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("error"), "Not Found");
    }

    @Test(priority = 1)
    public void EmailCaseSensitivityAndValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("EmailCaseSensitivityandPasswordLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("firstName"), "Yousef", "First name mismatch!");
        Assert.assertEquals(response.jsonPath().getString("email"), "hassaniengivers@gmail.com", "Email mismatch!");
        Assert.assertTrue(response.jsonPath().getBoolean("isEmailVerified"), "isEmailVerified should be True!");
        Assert.assertTrue(response.jsonPath().getBoolean("isAccountActive"),"isAccountActive should be True!");

        // Check that accessToken and refreshToken are not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("refreshToken"), "Refresh token should not be null!");
    }



}
