package tests.CommunityAPIs.NGO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AddNGOMember {

    private String authToken;

    @BeforeTest
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
    public void AddNGOMemberToNGOCommunitySuccessfulTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";
        String userId = "31";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 201);

    }

    @Test(priority = 2)
    public void AddNGOMemberToNonNGOCommunityTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "1";
            String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community is not an NGO", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void AddNGOMemberToNonExistNGOCommunityTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "99999";
        String userId = "31";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found.", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void AddNGOMemberToInvalidNGOCommunityTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "abc";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void AddNGOMemberToNGOWithInvalidUserIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";
        String userId = "xyz";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "userId is required and must be a number.", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void AddNGOMemberToNGOWithMissingTokenTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");
    }

    @Test(priority = 7)
    public void AddNGOMemberToNGOWithWhoIsAlreadyMemberTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "User is already an NGO community member", "Error message mismatch!");
    }

    @Test(priority = 8)
    public void AddNGOMemberToNGOWithMissingCommunityIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = " ";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 9)
    public void AddNGOMemberrToNGOWithMissingUserIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/ngo-members");

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Required", "Error message mismatch!");
    }

}
