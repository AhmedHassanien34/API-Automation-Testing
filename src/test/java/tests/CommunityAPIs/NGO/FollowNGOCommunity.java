package tests.CommunityAPIs.NGO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FollowNGOCommunity {

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
    public void FollowNGOSuccessfullyTest() {

        String communityId = "33"; // Community ID from the path variable in the image

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/follow");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/follow");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 2)
    public void FollowCommunityTest() {

        String communityId = "1"; // Community ID from the path variable in the image

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/follow");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/follow");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community is not an NGO", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void FollowNGOWithNonExistNGOTest() {

        String communityId = "9999"; // Community ID from the path variable in the image

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/follow");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/follow");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void FollowNGOWithInvalidNGOIdTest() {

        String communityId = "abc"; // Community ID from the path variable in the image

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/follow");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/follow");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void FollowNGOWithMissingTokenTest() {

        String communityId = "28"; // Community ID from the path variable in the image

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/follow");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/follow");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void FollowNGOWithMissingCommunityIdTest() {

        String communityId = " "; // Community ID from the path variable in the image

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/communities/"+ communityId + "/follow");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/follow");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }


}
