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

public class UnlikeCommunityPost {


    private String authToken;
    public static String PostId;


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
    public void UnLikePostSuccessfullyTest() throws IOException {

        PostId = "38";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete("/api/posts/" + PostId +"/likes"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

    }

    @Test(priority = 2)
    public void UnLikePostWithAlreadyUnLikedTest() throws IOException {

        PostId = "38";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete("/api/posts/" + PostId +"/likes"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Post like not found", "Error message mismatch!");

    }

    @Test(priority = 3)
    public void UnLikePostWithNotExistPostTest() throws IOException {

        PostId = "99999";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete("/api/posts/" + PostId +"/likes"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Post not found", "Error message mismatch!");

    }
    @Test(priority = 3)
    public void UnLikePostWithInvalidPostIdTest() throws IOException {

        PostId = "abc";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete("/api/posts/" + PostId +"/likes"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "Parameter path \"id\" must be valid number.", "Error message mismatch!");

    }


    @Test(priority = 4)
    public void UnLikePostWithMissingPostIdTest() throws IOException {


        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete("/api/posts/likes"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Cannot DELETE /api/posts/likes", "Error message mismatch!");

    }

    @Test(priority = 5)
    public void UnLikePostWithMissingTokenTest() throws IOException {

        PostId = "38";

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .delete("/api/posts/" + PostId +"/likes"); // Replace with actual endpoint

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage1, "Unauthorized", "Error message mismatch!");

    }

}
