package tests.AuthenticationAPIs;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class ResetPasswordConfirmApiTest {

    @Test(priority = 1)
    public void ResetPasswordConfirm() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordConfirm");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password-confirm", requestBody);

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/auth/reset-password-confirm");
            System.out.println("Request Payload: ResetPasswordConfirm");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "Password changed successfully");

    }

    @Test(priority = 2)
    public void ResetPasswordConfirmMissingEmail() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordConfirmMissingEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password-confirm", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password-confirm");
            System.out.println("Request Payload: ResetPasswordConfirmMissingEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 400
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors[0].message"), "Email is required.");
    }


    @Test(priority = 3)
    public void ResetPasswordConfirmMissingPassword() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordConfirmMissingPassword");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password-confirm", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password-confirm");
            System.out.println("Request Payload: ResetPasswordConfirmMissingPassword");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors[0].message"), "Password is required.");
    }

    @Test(priority = 4)
    public void ResetPasswordConfirmMissingOTP() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordConfirmMissingOTP");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password-confirm", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password-confirm");
            System.out.println("Request Payload: ResetPasswordConfirmMissingOTP");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors[0].message"), "OTP is required.");
    }

    @Test(priority = 5)
    public void ResetPasswordConfirmInvalidOTP() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordConfirmInvalidOTP");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password-confirm", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password-confirm");
            System.out.println("Request Payload: ResetPasswordConfirmInvalidOTP");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors[0].message"), "OTP must contain only digits");
    }

    @Test(priority = 6)
    public void ResetPasswordConfirmWeakPassword() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordConfirmWeakPassword");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password-confirm", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password-confirm");
            System.out.println("Request Payload: ResetPasswordConfirmWeakPassword");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("errors[0].message"), "Password must contain a minimum of 8 characters, a special character, number, and letter.");
    }


    @Test(priority = 7)
    public void ResetPasswordConfirmNon_RegisteredEmail() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ResetPasswordConfirmNon_RegisteredEmail");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/reset-password-confirm", requestBody);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/reset-password-confirm");
            System.out.println("Request Payload: ResetPasswordConfirm");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate specific fields in the response body
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid verification attempt. Please try again.");
    }

}

