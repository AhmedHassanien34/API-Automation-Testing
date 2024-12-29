package tests.AuthenticationAPIs;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class VerifyOtpApiTest {

    @Test(priority = 1)
    public void InValidOTPVerificationTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("InValidOTPVerification");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/verify-otp/", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/verify-otp");
            System.out.println("Request Payload: InValidOTPVerification");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Invalid verification attempt. Please try again.", "Error message mismatch!");
    }

    @Test(priority = 2)
    public void ValidOTPVerificationTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ValidOTPVerification");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/verify-otp/", requestBody);

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/auth/verify-otp");
            System.out.println("Request Payload: ValidOTPVerification");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "OTP verified successfully", "Error message mismatch!");
    }
}
