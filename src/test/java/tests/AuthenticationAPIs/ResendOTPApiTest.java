package tests.AuthenticationAPIs;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class ResendOTPApiTest {

    @Test(priority = 1)
    public void ResendOTPWithValidEmailTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResendOTPWithValidEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/resend-otp/", requestBody);

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/auth/resend-otp");
            System.out.println("Request Payload: ResendOTPWithValidEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "If an account exists for this email, we've sent a one-time password to it.", "Error message mismatch!");
    }

    @Test(priority = 2)
    public void ResendOTPWithInvalidEmailTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResendOTPWithInvalidEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/resend-otp/", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/resend-otp");
            System.out.println("Request Payload: ResendOTPWithInvalidEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid email", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void ResendOTPWithEmailIsNotExist() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResendOTPWithEmailIsNotExist");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/resend-otp/", requestBody);

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/auth/resend-otp");
            System.out.println("Request Payload: ResendOTPWithEmailIsNotExist");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "If an account exists for this email, we've sent a one-time password to it.", "Error message mismatch!");
    }
/*
    @Test(priority = 4)
    public void ResendOTPWithEmptyBodyTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResendOTPWithEmptyBody");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/resend-otp/", requestBody);

            if (response.getStatusCode() != 400)
            {
                System.out.println("End Point: /api/auth/resend-otp");
                System.out.println("Request Payload: ResendOTPWithEmptyBody");
                System.out.println("Request Body:");
                response.prettyPrint();
            }

        // Assert the response status code is 400 (Bad Request)
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Required", "Error message mismatch!");
    }


 */
}

