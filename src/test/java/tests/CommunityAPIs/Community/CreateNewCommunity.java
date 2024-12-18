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

public class CreateNewCommunity {


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
    public void CreateNewCommunitySuccessfullyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

    }

    @Test(priority = 2)
    public void CreateNewCommunityWithInvalidProfessionalEmailTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("InValidProfessionalEmailCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid email address", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void CreateNewCommunityWithMissingRequiredFieldsTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("MissingRequiredFieldsCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Required", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void CreateNewCommunityWithCommunityNameExceedsCharacterLimitTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("CommunityNameExceedsCharacterLimitCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Name must be at most 50 characters long", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void CreateNewCommunityWithEmptyCoverPhotoURLTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EmptyCoverPhotoURLCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid url", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void CreateNewCommunityWithInvalidCoverPhotoURLTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("InvalidCoverPhotoURLCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid url", "Error message mismatch!");
    }

    @Test(priority = 7)
    public void CreateNewCommunityWithDuplicateNameTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("DuplicateNameCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "A community with that name already exists.", "Error message mismatch!");
    }

    @Test(priority = 8)
    public void CreateNewCommunityWithSpecialCharactersCommunityNameTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("SpecialCharactersCommunityNameCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Name can only contain Arabic letters, English letters, and spaces", "Error message mismatch!");
    }

    @Test(priority = 9)
    public void CreateNewCommunityWithMissingTokenTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidCreationCommunity");

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Access token is required ", "Error message mismatch!");
    }






}
