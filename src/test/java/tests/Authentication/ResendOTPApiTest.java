package tests.Authentication;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class ResendOTPApiTest {

    @Test(priority = 1)
    public void ResendOTPVerificationTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("InValidOTPVerification");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/verify-otp/"+ "hassaniengivers+5009@gmail.com", requestBody);

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Invalid verification attempt. Please try again.", "Error message mismatch!");
    }

    @Test(priority = 2)
    public void InValidOTPVerificationTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("InValidOTPVerification");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/verify-otp/"+ "hassaniengivers+111@gmail.com", requestBody);

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Invalid verification attempt. Please try again.", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void ResendOTPWithEmailIsNotExist() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResendOTPWithEmailIsNotExist");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/resend-otp/"+ "notregistered@example.com", requestBody);

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "If an account exists for this email, we've sent a one-time password to it.", "Error message mismatch!");
    }
}

