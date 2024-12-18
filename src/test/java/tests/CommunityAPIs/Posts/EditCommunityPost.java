package tests.CommunityAPIs.Posts;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class EditCommunityPost {

    AddCommunityPost addCommunityPost  = new AddCommunityPost();

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
    public void EditCommunityPostSuccessfullyTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionCommunityPost");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(priority = 2)
    public void AddCommunityPostWithInvalidMediaFileFormatTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityPostWithInvalidMediaFileFormat");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage1, "media", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].path[2]");
        Assert.assertEquals(errorMessage2, "filename", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage3 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage3, "String must contain at least 1 character(s)", "Error message mismatch!");

    }


    @Test(priority = 3)
    public void AddCommunityPostWithInvalidMediaFilePathTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityPostWithInvalidMediaFilePath");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage1, "media", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].path[2]");
        Assert.assertEquals(errorMessage2, "filepath", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage3 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage3, "File path for documents must be a URL", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void AddCommunityPostWithEmptyBodyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityPostWithEmptyBody");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        System.out.println("Response Body: " + response.getBody().asString());

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

    }

    @Test(priority = 5)
    public void AddCommunityPostWithNoMediaTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityPostWithNoMedia");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        System.out.println("Response Body: " + addCommunityPost.PostId + "  " + response.getBody().asString());

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

    }

    @Test(priority = 6)
    public void AddCommunityPostWithLargePostContentTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommunityPostWithLargePostContent");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

    }

    @Test(priority = 7)
    public void EditCommunityPostWithPostInDifferentCommunity() throws IOException {

        addCommunityPost.PostId = "11";

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionCommunityPost");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 403, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Forbidden", "Error message mismatch!");
    }


    @Test(priority = 8)
    public void EditCommunityPostWithMissingPostId() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionCommunityPost");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Cannot PATCH /api/posts/", "Error message mismatch!");
    }


    @Test(priority = 9)
    public void EditCommunityPostWithNonOwnerThePost() throws IOException {

        addCommunityPost.PostId = "43";

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionCommunityPost");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 403, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Forbidden", "Error message mismatch!");
    }

    @Test(priority = 10)
    public void EditCommunityPostWithInvalidPostId() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionCommunityPost");

        addCommunityPost.PostId = "abc";
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 11)
    public void EditCommunityPostWithNonExistPostId() throws IOException {

        addCommunityPost.PostId = "99999";

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionCommunityPost");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Post not found", "Error message mismatch!");
    }


    @Test(priority = 12)
    public void EditCommunityPostWithMissingTokenTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionCommunityPost");
        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/posts/" + addCommunityPost.PostId ); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

    }


}
