package tests.AuthenticationAPIs;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class ResetPasswordApiTest {

    @Test(priority = 1)
    public void ResetPasswordTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password", requestBody);

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/auth/reset-password");
            System.out.println("Request Payload: ResetPasswordEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "If an account exists for this email address, we have sent a password reset OTP.");

    }

    @Test(priority = 2)
    public void ResetPasswordMissingEmailTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordEmailMissingEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password");
            System.out.println("Request Payload: ResetPasswordEmailMissingEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors.message"), "[Email is required.]");

    }

    @Test(priority = 3)
    public void ResetPasswordInvalidEmaiTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordInvalidEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password");
            System.out.println("Request Payload: ResetPasswordInvalidEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors.message"), "[Invalid email address]");

    }

    @Test(priority = 4)
    public void ResetPasswordNon_RegisteredEmaiTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordEmailNon_RegisteredEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password", requestBody);

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/auth/reset-password");
            System.out.println("Request Payload: ResetPasswordEmailNon_RegisteredEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "If an account exists for this email address, we have sent a password reset OTP.");

    }

    @Test(priority = 5)
    public void ResetPasswordEmailExceedsMaximumLengthTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordEmailExceedsMaximumLength");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password");
            System.out.println("Request Payload: ResetPasswordEmailExceedsMaximumLength");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode (), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "If an account exists for this email address, we have sent a password reset OTP.");

    }

    @Test(priority = 6)
    public void ResetPasswordEmailEmptyFieldTest() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordEmailEmptyField");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password");
            System.out.println("Request Payload: ResetPasswordEmailEmptyField");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors.message"), "[Invalid email address]");

    }
}
