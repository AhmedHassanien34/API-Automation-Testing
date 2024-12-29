package tests.CommunityAPIs.NGO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SendNGOMemberInvitation {

    private String authToken;

    @BeforeClass
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ModeratorLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");  // Adjust "token" based on your JSON structure
    }


    @Test(priority = 1)
    public void SendNGOMemberInvitationToAcceptTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "192";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 201);

    }

    @Test(priority = 2)
    public void SendNGOMemberInvitationToRejectTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "193";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 201);

    }

    @Test(priority = 3)
    public void SendNGOMemberDuplicateInvitationTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "193";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 409)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 409);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "There is already a pending invitation for that user", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void SendNGOMemberInvitationWithInvalidCommunityIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "99999";
        String userId = "193";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
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
    public void SendNGOMemberInvitationWithInvalidUserIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "999999";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "User you're trying to invite doesn't exist", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void SendNGOMemberInvitationWithMissingUserIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "99999";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Required", "Error message mismatch!");

        // Validate the error message in the response body
      /*  String errorMessage = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage, "userId", "Error message mismatch!"); */
    }

    @Test(priority = 7)
    public void SendNGOMemberInvitationWithNegativeCommunityIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "-433";
        String userId = "193";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");

        // Validate the error message in the response body
      /*  String errorMessage = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage, "communityId", "Error message mismatch!"); */
    }

    @Test(priority = 8)
    public void SendNGOMemberInvitationWithNegativeUserIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "-193";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");

        // Validate the error message in the response body
      /*  String errorMessage = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage, "userId", "Error message mismatch!"); */
    }

    @Test(priority = 9)
    public void SendNGOMemberInvitationToUserWhoAlreadyMemberTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "417";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 409)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 409);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "User is already a member of this NGO community", "Error message mismatch!");

        // Validate the error message in the response body
      /*  String errorMessage = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage, "userId", "Error message mismatch!"); */
    }

    @Test(priority = 10)
    public void SendNGOMemberInvitationToUserWhoIsModeratorTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "188";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "The User invited is a moderator", "Error message mismatch!");

    }

    @Test(priority = 11)
    public void SendNGOMemberInvitationToUserWhoIsMemberInAnotherNGOTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "User is already a member of the NGO community", "Error message mismatch!");

    }

    @Test(priority = 12)
    public void SendNGOMemberInvitationWithMissingTokenTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "192";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");

    }

    @Test(priority = 13)
    public void SendNGOMemberInvitationToUserWhoIsMNotModeratorInNGOTest() throws IOException {

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "96";

        Map<String, Object> requestBody = RestClient.getTestData("Member1Login");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");  // Adjust "token" based on your JSON structure

        // Sending POST request
        Response response2 = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members/invite");

        if (response2.getStatusCode() != 403)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members/invite");
            System.out.println("Request Body:");
            response2.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response2.getStatusCode(), 403);

        // Validate the error message in the response body
        String errorMessage = response2.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Only moderators allowed to add NGO members", "Error message mismatch!");

    }



}
