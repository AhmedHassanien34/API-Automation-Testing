package tests.CommunityAPIs.Comments;

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

public class AddComment {

    private String authToken;
    public static String CommentId;

    @BeforeClass
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ValidEmailandPassword");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");
    }


    @Test(priority = 1)
    public void AddCommentToPostSuccessfullyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidCreationCommentToPost");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidCreationCommentToPost");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that id, communityId, userId, isModeratorPost, isPinned and createdAt are not null
        Assert.assertNotNull(response.jsonPath().getString("id"), "id should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("text"), "communityId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("createdAt"), "userId should not be null!");

        CommentId = response.jsonPath().getString("id");  // Adjust "id" based on your JSON structure

    }

    @Test(priority = 2)
    public void AddCommentToPostWithMissingContentTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommentToPostWithMissingContent");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: AddCommentToPostWithMissingContent");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage1, "[text]", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Required", "Error message mismatch!");

    }

    @Test(priority = 3)
    public void AddCommentToPostWithMissingPostIdTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommentToPostWithMissingPostId");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: AddCommentToPostWithMissingPostId");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage1, "[postId]", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Expected number, received nan", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void AddCommentToPostWithInvalidPostIdTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommentToPostWithInvalidPostId");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: AddCommentToPostWithInvalidPostId");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage1, "[postId]", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Expected number, received nan", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void AddCommentToPostWithNonExistPostIdTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommentToPostWithNonExistPostId");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: AddCommentToPostWithNonExistPostId");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");


        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage2, "Post not found", "Error message mismatch!");

    }

    @Test(priority = 7)
    public void AddCommentToPostWithEmptyTextTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommentToPostWithEmptyText");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: AddCommentToPostWithEmptyText");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage1, "text", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "String must contain at least 2 character(s)", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage3 = response.jsonPath().getString("errors[1].path[0]");
        Assert.assertEquals(errorMessage3, "text", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage4 = response.jsonPath().getString("errors[1].message");
        Assert.assertEquals(errorMessage4, "Text cannot be empty or contain only whitespace", "Error message mismatch!");

    }


    @Test(priority = 9)
    public void AddCommentToPostWithLargeCommentTextTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommentToPostWithLargeCommentText");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: AddCommentToPostWithLargeCommentText");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage1, "text", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "String must contain at most 500 character(s)", "Error message mismatch!");


    }


    @Test(priority = 10)
    public void AddCommentToPostWithMissingTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidCreationCommentToPost");

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/comments");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidCreationCommentToPost");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage2, "Unauthorized", "Error message mismatch!");

    }
}
