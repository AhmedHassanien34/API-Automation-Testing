package tests.CommunityAPIs.Posts;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tests.CommunityAPIs.Community.CreateNewCommunity;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AddCommunityPost {

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
    public void AddCommunityPostSuccessfullyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidCreationCommunityPost");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: ValidCreationCommunityPost");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that id, communityId, userId, isModeratorPost, isPinned and createdAt are not null
        Assert.assertNotNull(response.jsonPath().getString("id"), "id should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("communityId"), "communityId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("userId"), "userId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isModeratorPost"), "isModeratorPost should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isPinned"), "isPinned should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("createdAt"), "createdAt should not be null!");

        PostId = response.jsonPath().getString("id");  // Adjust "id" based on your JSON structure

    }

    @Test(priority = 2)
    public void AddCommunityPostWithMissingContentTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithMissingContent");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithMissingContent");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage1, "[content]", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Required", "Error message mismatch!");

    }

    @Test(priority = 3)
    public void AddCommunityPostWithMissingCommunityIdTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithMissingCommunityId");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithMissingCommunityId");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path");
        Assert.assertEquals(errorMessage1, "[communityId]", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Expected number, received nan", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void AddCommunityPostWithInvalidCommunityIdTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithInvalidCommunityId");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithInvalidCommunityId");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage2, "Community not found.", "Error message mismatch!");

    }

    @Test(priority = 5)
    public void AddCommunityPostWithInvalidMediaFileFormatTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithInvalidMediaFileFormat");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithInvalidMediaFileFormat");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

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


    @Test(priority = 6)
    public void AddCommunityPostWithInvalidMediaFilePathTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithInvalidMediaFilePath");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithInvalidMediaFilePath");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

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

    @Test(priority = 7)
    public void AddCommunityPostWithEmptyBodyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithEmptyBody");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithEmptyBody");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage1, "communityId", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Expected number, received nan", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage3 = response.jsonPath().getString("errors[1].path[0]");
        Assert.assertEquals(errorMessage3, "content", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage4 = response.jsonPath().getString("errors[1].message");
        Assert.assertEquals(errorMessage4, "Required", "Error message mismatch!");

    }

    @Test(priority = 8)
    public void AddCommunityPostWithNoMediaTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithNoMedia");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithNoMedia");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that id, communityId, userId, isModeratorPost, isPinned and createdAt are not null
        Assert.assertNotNull(response.jsonPath().getString("id"), "id should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("communityId"), "communityId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("userId"), "userId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isModeratorPost"), "isModeratorPost should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isPinned"), "isPinned should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("createdAt"), "createdAt should not be null!");

    }

    @Test(priority = 9)
    public void AddCommunityPostWithLargePostContentTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("AddCommunityPostWithLargePostContent");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: AddCommunityPostWithLargePostContent");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

    }


    @Test(priority = 10)
    public void AddCommunityPostWithMissingTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidCreationCommunityPost");

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/posts");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/posts");
            System.out.println("Request Payload: ValidCreationCommunityPost");
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
