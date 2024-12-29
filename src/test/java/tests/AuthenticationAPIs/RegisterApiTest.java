package tests.AuthenticationAPIs;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class RegisterApiTest {

    @Test(priority = 1)
        public void SuccessfulRegisterTest() throws IOException {
            // Get test data for valid registration
            Map<String, Object> requestBody = RestClient.getTestData("validRegisterRequest");

            // Send POST request to register endpoint
            Response response = RestClient.postRequest("/api/auth/register", requestBody);

            if (response.getStatusCode() != 201)
            {
                System.out.println("End Point: /api/auth/register");
                System.out.println("Request Payload: validRegisterRequest");
                System.out.println("Request Body:");
                response.prettyPrint();
            }

            // Assert the response status code is 201
            Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");


            // Validate specific fields in the response body
            Assert.assertEquals(response.jsonPath().getString("firstName"), "A", "First name mismatch!");
            Assert.assertEquals(response.jsonPath().getString("email"), "hassaniengivers+5000@gmail.com", "Email mismatch!");
            Assert.assertFalse(response.jsonPath().getBoolean("isEmailVerified"), "isEmailVerified should be false!");
            Assert.assertFalse(response.jsonPath().getBoolean("isAccountVerified"),"isAccountActive should be false!");

            // Check that accessToken and refreshToken are not null
            Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");
            Assert.assertNotNull(response.jsonPath().getString("refreshToken"), "Refresh token should not be null!");

            // Validate response body (for example, check if registration was successful)
            String isEmailVerified = response.jsonPath().getString("isEmailVerified");
            Assert.assertEquals(isEmailVerified, "false");
        }

    @Test(priority = 2)
    public void InvalidEmailFormatTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("InvalidRegisterRequest");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/register", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/register");
            System.out.println("Request Payload: InvalidRegisterRequest");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid email address", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void RegisterUserWeakPasswordTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("WeakPasswordRequest");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/register", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/register");
            System.out.println("Request Payload: WeakPasswordRequest");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Password must contain a minimum of 8 characters, a special character, number, and letter.", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void RegisterUserEmailAlreadyExistsTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("EmailAlreadyExistsRegisterRequest");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/register", requestBody);

        if (response.getStatusCode() != 409)
        {
            System.out.println("End Point: /api/auth/register");
            System.out.println("Request Payload: EmailAlreadyExistsRegisterRequest");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 409, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "User's email already exists", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void RegisterUserFirstNameExceedingCharacterLimitTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("FirstNameExceedingCharacterLimitRequest");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/register", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/register");
            System.out.println("Request Payload: FirstNameExceedingCharacterLimitRequest");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "First name cannot exceed 20 characters.", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void RegisterUserLastNameExceedingCharacterLimitTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("LastNameExceedingCharacterLimitRequest");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/register", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/register");
            System.out.println("Request Payload: LastNameExceedingCharacterLimitRequest");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Last name cannot exceed 20 characters.", "Error message mismatch!");
    }
}
