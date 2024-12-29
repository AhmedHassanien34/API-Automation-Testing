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

public class EditComment {


    String CommentId;

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
    public void EditCommentSuccessfullyTest() throws IOException {

        CommentId  = "158";

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionComment");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/" + CommentId );

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidEditionComment");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

    }


    @Test(priority = 2)
    public void EditCommentWithEmptyTextTest() throws IOException {

        CommentId  = "48";

        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommentWithEmptyText");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/" + CommentId );

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: EditCommentWithEmptyText");
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

    @Test(priority = 3)
    public void EditCommentWithLargeTextTest() throws IOException {

        CommentId  = "48";

        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EditCommentWithLargeText");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/" + CommentId );

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: EditCommentWithLargeText");
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


    @Test(priority = 4)
    public void EditCommentWithMissingCommentId() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionComment");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidEditionComment");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Cannot PATCH /api/comments/", "Error message mismatch!");
    }


    @Test(priority = 5)
    public void EditCommentWithNonOwnerTheComment() throws IOException {

        CommentId = "4";

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionComment");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/" + CommentId );

        if (response.getStatusCode() != 403)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidEditionComment");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 403, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Forbidden", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void EditCommentWithInvalidCommentId() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionComment");

        CommentId = "abc";
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/" + CommentId );

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidEditionComment");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 7)
    public void EditCommentPostWithNonExistCommentId() throws IOException {

        CommentId = "99999";

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionComment");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/" + CommentId );

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidEditionComment");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Comment not found", "Error message mismatch!");
    }


    @Test(priority = 8)
    public void EditCommentWithMissingTokenTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("ValidEditionComment");
        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/comments/" + CommentId );

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/comments");
            System.out.println("Request Payload: ValidEditionComment");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

    }
}
