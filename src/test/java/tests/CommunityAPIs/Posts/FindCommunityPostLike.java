package tests.CommunityAPIs.Posts;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FindCommunityPostLike {


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
    public void FindCommunityPostLikesSuccessfullyTest() throws IOException {

        String PostId = "10";

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/posts/" + PostId +"/likes");


        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

        // Validate specific fields in the response body
        Assert.assertNotNull(response.jsonPath().getString("[0].userFirstName"), "userFirstName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("[0].userLastName"), "userLastName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("[0].userAvatar"), "userAvatar should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("[0].numOfSupportedNeedies"), "numOfSupportedNeedies should not be null!");
    }

    @Test(priority = 2)
    public void FindCommunityPostLikesWithNotExistPostIdTest() throws IOException {

        String PostId = "99999";

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);


        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Post not found", "Error message mismatch!");

    }


    @Test(priority = 3)
    public void FindCommunityPostLikesWithInvalidPostIdTest() throws IOException {

        String PostId = "abc";

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);


        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/posts/" + PostId +"/likes");


        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validate the response status code
        response.then().statusCode(400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void FindCommunityPostLikesWithISpecialCharacterPostIdTest() throws IOException {

        String PostId = "@!#$";

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);


        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");

    }

    @Test(priority = 5)
    public void FindCommunityPostLikesWithNoLikesOnPostTest() throws IOException {

        String PostId = "48";

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);


        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

    }

    @Test(priority = 6)
    public void FindCommunityPostLikesWithMissingTokenTest() throws IOException {

        String PostId = "48";

        RequestSpecification request = given();

        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/posts/" + PostId +"/likes");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/posts/" + PostId +"/likes");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");
    }

}
