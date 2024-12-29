package tests.CommunityAPIs.NGO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AcceptNGOMemberInvitation {

    private String authToken;

    @BeforeClass
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("Member1Login");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");  // Adjust "token" based on your JSON structure
    }


    @Test(priority = 1)
    public void AcceptNGOMemberInvitationTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members/accept");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/accept");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 201);

    }

    @Test(priority = 2)
    public void AcceptNGOMemberInvitationWithInvitationAlreadyAcceptedTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members/accept");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/accept");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "NGO membership invitation doesn't exist", "Error message mismatch!");
    }


    @Test(priority = 3)
    public void AcceptNGOMemberInvitationWithInvalidCommunityIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "-433";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members/accept");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/accept");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");


    }

    @Test(priority = 4)
    public void AcceptNGOMemberInvitationWithNonExistCommunityIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "999999";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members/accept");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/accept");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found.", "Error message mismatch!");


    }

    @Test(priority = 5)
    public void AcceptNGOMemberInvitationWithMissingCommunityIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = " ";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members/accept");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/accept");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void AcceptNGOMemberInvitationWithMissingTokenTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members/accept");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/accept");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");

    }
}
