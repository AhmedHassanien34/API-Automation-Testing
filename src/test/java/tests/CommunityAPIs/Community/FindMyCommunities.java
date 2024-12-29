package tests.CommunityAPIs.Community;

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

public class FindMyCommunities {

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
    public void FindMyCommunitiesSuccessfullyTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/communities/my");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/my");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

        // Validate specific fields in the response body
        Assert.assertNotNull(response.jsonPath().getString("data[0].communityId"), "communityId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data[0].communityName"), "communityName should not be null!");
//        Assert.assertNotNull(response.jsonPath().getString("[0].type"), "type should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data[0].membersCount"), "membersCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data[0].followersCount"), "followersCount should not be null!");
//        Assert.assertNotNull(response.jsonPath().getString("[0].donationsCount"), "donationsCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data[0].neediesSupportedCount"), "neediesSupportedCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data[0].createdByUserId"), "createdByUserId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data[0].createdByFirstName"), "membersCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data[0].createdByLastname"), "followersCount should not be null!");
//        Assert.assertNotNull(response.jsonPath().getString("[0].isMember"), "isMember should not be null!");
//        Assert.assertNotNull(response.jsonPath().getString("[0].isFollower"), "isFollower should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("data    [0].avatars"), "avatars should not be null!");

    }



    @Test(priority = 2)
    public void FindMyCommunitiesWithPaginationFunctionalityTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Add query parameters
        request.queryParam("page", 1);

        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/communities/my");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/my");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

    }

    @Test(priority = 4)
    public void FindMyommunitiesWithInvalidPageNumberTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Add query parameters
        request.queryParam("page", -1);

        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/communities/my");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/my");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Query parameter page must be a valid number", "Error message mismatch!");

    }

    @Test(priority = 5)
    public void FindAllCommunitiesWithNoCommunitiesTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Add query parameters
        request.queryParam("page", 9999);

        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/communities/my");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/my");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

    }

    @Test(priority = 6)
    public void FindAllCommunitiesWithMissingTokenTest() throws IOException {

        RequestSpecification request = given();

        // Add query parameters
        request.queryParam("page", 1);

        // Make a GET request to the endpoint and store the response
        Response response = request.get("/api/communities/my");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/my");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Access token is required", "Error message mismatch!");
    }
}
