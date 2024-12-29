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

public class LikeCommunityPost {


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
    public void LikePostSuccessfullyTest() throws IOException {

        PostId = "237";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

    }

    @Test(priority = 2)
    public void LikePostWithAlreadyLikedTest() throws IOException {

        PostId = "237";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "User already liked the post.", "Error message mismatch!");

    }

    @Test(priority = 3)
    public void LikePostWithNotExistPostTest() throws IOException {

        PostId = "99999";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Post doesn't exist", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void LikePostWithMissingPostIdTest() throws IOException {

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/posts/likes");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Cannot POST /api/posts/likes", "Error message mismatch!");

    }

    @Test(priority = 5)
    public void LikePostWithInvalidPostIdTest() throws IOException {

        PostId = "abc";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "Parameter path \"id\" must be valid number.", "Error message mismatch!");

    }

    @Test(priority = 5)
    public void LikePostWithMissingTokenTest() throws IOException {

        PostId = "38";

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Unauthorized", "Error message mismatch!");

    }

}
