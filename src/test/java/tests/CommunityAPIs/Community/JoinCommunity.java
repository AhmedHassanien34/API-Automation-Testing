package tests.CommunityAPIs.Community;

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

public class JoinCommunity {

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
    public void SuccessfullyJoinCommunityTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidJoinCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/join");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/join");
            System.out.println("Request Payload: ValidJoinCommunity");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "An OTP was sent to your professional email.", "Error message mismatch!");
    }

    @Test(priority = 2)
    public void JoinCommunityWithInValidEmailTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("JoinCommunityWithInValidEmail");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/join");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/join");
            System.out.println("Request Payload: JoinCommunityWithInValidEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid email", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void JoinCommunityWithMissingProfessionalEmailTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("JoinCommunityWithMissingProfessionalEmail");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/join");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/join");
            System.out.println("Request Payload: JoinCommunityWithMissingProfessionalEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Email is required", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void JoinCommunityWithExistProfessionalEmailTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("JoinCommunityWithExistProfessionalEmail");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/join");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/join");
            System.out.println("Request Payload: JoinCommunityWithExistProfessionalEmail");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "You are already a member of Amr's", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void JoinCommunityWithMissingTokenTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidJoinCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/join");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/join");
            System.out.println("Request Payload: ValidJoinCommunity");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Access token is required", "Error message mismatch!");
    }


    @Test(priority = 6)
    public void JoinCommunityWithSendManyOTPsTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("JoinCommunityWithMissingProfessionalEmail");

        Response response = null;
        // Send POST request 4 times sequentially
        for (int i = 1; i <= 5; i++) {
            response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/join");

            if (response.getStatusCode() != 201)
            {
                System.out.println("End Point: /api/communities/join");
                System.out.println("Request Payload: JoinCommunityWithMissingProfessionalEmail");
                System.out.println("Request Body:");
                response.prettyPrint();
            }


            // Print the response status code
            System.out.println("Attempt " + i + " - Status Code: " + response.getStatusCode());
            System.out.println("Response: " + response.getBody().asString());

            // Check if rate limit (429) is reached
            if (response.getStatusCode() == 429) {
                System.out.println("Rate limit reached on attempt " + i);
                break;
            }
        }

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "ThrottlerException: Too Many Requests", "Error message mismatch!");
    }


}
