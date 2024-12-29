package tests.CommunityAPIs.Comments;

import com.thoughtworks.qdox.model.expression.Add;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tests.CommunityAPIs.Community.CreateNewCommunity;
import tests.CommunityAPIs.Posts.AddCommunityPost;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class DeleteComment {

    AddComment CommentId  = new AddComment();

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
    public void DeleteCommentFromPostSuccessfullyTest() throws IOException {

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/comments/" + AddComment.CommentId );

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/comments/" + AddComment.CommentId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(priority = 2)
    public void DeleteCommunityPostWithAlreadyDeletedTest() throws IOException {

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/comments/" + AddComment.CommentId );

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/comments/" + AddComment.CommentId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Comment not found.", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void DeleteCommentFromPostWithInvalidCommentId() throws IOException {

        AddComment.CommentId = "abc";
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/comments/" + AddComment.CommentId );

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/comments/" + AddComment.CommentId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void DeleteCommentFromPostWithNonExistCommentId() throws IOException {

        AddComment.CommentId = "99999";
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/comments/" + AddComment.CommentId );

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/comments/" + AddComment.CommentId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Comment not found.", "Error message mismatch!");
    }


    @Test(priority = 5)
    public void DeleteCommentFromPostWithMissingCommentId() throws IOException {

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/comments/");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/comments/" + AddComment.CommentId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Cannot DELETE /api/comments/", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void DeleteCommentFromPostWithNonOwnerTheComment() throws IOException {

        AddComment.CommentId = "4";
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/comments/" + AddComment.CommentId );

        if (response.getStatusCode() != 403)
        {
            System.out.println("End Point: /api/comments/" + AddComment.CommentId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 403, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Forbidden", "Error message mismatch!");
    }

    @Test(priority = 7)
    public void DeleteCommentFromPostWithMissingTokenTest() throws IOException {

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .delete ("/api/comments/" + AddComment.CommentId );

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/comments/" + AddComment.CommentId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

    }


}
