package tests.CommunityAPIs.Posts;

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

public class FindCommunityPost {

    String addCommunityPost;

    private String authToken;
    public static String PostId;


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
    public void FindCommunityPostSuccessfullyTest() throws IOException {

        addCommunityPost = "48";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .get("/api/posts/" + addCommunityPost);

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/posts/" + addCommunityPost);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Check that id, communityId, userId, isModeratorPost, isPinned and createdAt are not null
        Assert.assertNotNull(response.jsonPath().getString("postId"), "postId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isModeratorPost"), "isModeratorPost should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("communityId"), "communityId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("userId"), "userId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("postContent"), "postContent should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("attachments"), "attachments should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("userFirstName"), "userFirstName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("userLastName"), "userLastName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("userPicture"), "userPicture should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("numOfLikes"), "numOfLikes should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("numOfComments"), "numOfComments should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("postDate"), "postDate should not be null!");

    }

    @Test(priority = 2)
    public void FindCommunityPostWithInvalidPostId() throws IOException {

        addCommunityPost = "abc";
        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .get("/api/posts/" + addCommunityPost);

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/" + addCommunityPost);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void FindCommunityPostWithNonExistPostId() throws IOException {

        addCommunityPost = "99999";


        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .get("/api/posts/" + addCommunityPost);

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/posts/" + addCommunityPost);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Post not found", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void FindCommunityPostWithMissingTokenTest() throws IOException {

        addCommunityPost = "48";

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("/api/posts/" + addCommunityPost);

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/posts/" + addCommunityPost);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

    }
}
