package tests.CommunityAPIs.Community;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class EditCommunity {

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
    public void EditCommunitySuccessfullyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithCoverPhotoAndDescription");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

    }

    @Test(priority = 2)
    public void EditCommunityWithDescriptionOnlyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithDescriptionOnly");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(priority = 3)
    public void EditCommunityWithCoverPhotoOnlyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithCoverPhotoOnly");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(priority = 4)
    public void EditCommunityWithInvalidCommunityTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithCoverPhotoAndDescription");

        String CommunityId = "abc";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void EditCommunityWithNonExistCommunityTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithCoverPhotoAndDescription");

        String CommunityId = "9999";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void EditCommunityWithEmptyBodyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithEmptyBody");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(priority = 6)
    public void EditCommunityWithDescriptionHaveNumbersTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithDescriptionHaveNumbers");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "A description of the community is required", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void CreateNewCommunityWithEmptyCoverPhotoURLTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithEmptyCoverPhotoURL");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid url", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void CreateNewCommunityWithInvalidCoverPhotoURLTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithInvalidCoverPhotoURL");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid url", "Error message mismatch!");
    }

    @Test(priority = 7)
    public void EditCommunityWithMissingTokenTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithCoverPhotoAndDescription");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");
    }

    @Test(priority = 8)
    public void EditCommunityWithDescriptionExceedLimitTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithDescriptionExceedLimit");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Description exceeds maximum character limit.", "Error message mismatch!");
    }

    @Test(priority = 9)
    public void EditCommunityWithDescriptionHaveSpecialCharactersTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityWithDescriptionHaveSpecialCharacters");

        String CommunityId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/communities/" + CommunityId); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }





}
