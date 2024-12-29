package tests.AuthenticationAPIs; // Adjust the package name as per your project structure

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

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/auth/login");
            System.out.println("Request Payload: ValidEmailandPassword");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("firstName"), "Ahmed", "First name mismatch!");
        Assert.assertEquals(response.jsonPath().getString("email"), "hassaniengivers+2@gmail.com", "Email mismatch!");
        Assert.assertTrue(response.jsonPath().getBoolean("isEmailVerified"), "isEmailVerified should be True!");
        Assert.assertTrue(response.jsonPath().getBoolean("isAccountVerified"),"isAccountActive should be True!");

        // Check that accessToken and refreshToken are not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("refreshToken"), "Refresh token should not be null!");
    }

    @Test(priority = 2)
    public void MissingEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("MissingEmailandValidPasswordLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/login");
            System.out.println("Request Payload: MissingEmailandValidPasswordLogin");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "Email is required");
    }

    @Test(priority = 3)
    public void InvalidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ValidEmailandInvalidPasswordLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/auth/login");
            System.out.println("Request Payload: ValidEmailandInvalidPasswordLogin");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "Email or password is incorrect");
    }

    @Test(priority = 4)
    public void LoginWithUserIsnotExist() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("UserIsnotExistLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/auth/login");
            System.out.println("Request Payload: UserIsnotExistLogin");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("error"), "Not Found");
    }

    @Test(priority = 5)
    public void EmailCaseSensitivityAndValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("EmailCaseSensitivityandPasswordLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/auth/login");
            System.out.println("Request Payload: EmailCaseSensitivityandPasswordLogin");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("firstName"), "Yousef", "First name mismatch!");
        Assert.assertEquals(response.jsonPath().getString("email"), "hassaniengivers@gmail.com", "Email mismatch!");
        Assert.assertTrue(response.jsonPath().getBoolean("isEmailVerified"), "isEmailVerified should be True!");
        Assert.assertTrue(response.jsonPath().getBoolean("isAccountVerified"),"isAccountActive should be True!");

        // Check that accessToken and refreshToken are not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("refreshToken"), "Refresh token should not be null!");
    }



}
