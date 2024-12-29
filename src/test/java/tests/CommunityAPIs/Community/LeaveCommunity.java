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

public class LeaveCommunity {

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
    public void SuccessfullyLeaveJoinedCommunityTest() throws IOException {

        String CommunityId = "69";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ CommunityId +"/leave");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/"+ CommunityId +"/leave");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "You have left the community.", "Error message mismatch!");
    }

    @Test(priority = 2)
    public void LeaveCommunityWithNonExistentCommunityIdTest() throws IOException {
        // Get test data for valid registration
        String CommunityId = "9999";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ CommunityId +"/leave");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ CommunityId +"/leave");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found.", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void LeaveCommunityWithInvalidCommunityIdTest() throws IOException {
        // Get test data for valid registration
        String CommunityId = "abc";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ CommunityId +"/leave");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ CommunityId +"/leave");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void LeaveCommunityWithMemberAlreadyLeftCommunityTest() throws IOException {
        // Get test data for valid registration
        String CommunityId = "69";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ CommunityId +"/leave");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ CommunityId +"/leave");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "User is not a member of the community", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void LeaveCommunityWithMissingTokenTest() throws IOException {
        // Get test data for valid registration
        String CommunityId = "1";

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ CommunityId +"/leave");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/"+ CommunityId +"/leave");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Access token is required", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void JoinCommunityWithMissingCommunityIdTest() throws IOException {
        // Get test data for valid registration
        String CommunityId = " ";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ CommunityId +"/leave");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ CommunityId +"/leave");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

}
