package tests.CommunityAPIs.Community;

import io.restassured.response.Response;
import io.restassured.specification.Argument;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Predicates.equalTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ShowCommunityDetails {

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
    public void ShowCommunityDetailsSuccessfullyTest() throws IOException {

    // Path parameter for community ID
        String communityId = "69";

    // Sending GET request to /api/communities/{id}
    RequestSpecification request = given()
            .header("Authorization", "Bearer " + authToken)
            .header("Content-Type", "application/json"); // Add any required headers

    Response response = request.get("/api/communities/" + communityId);

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/" + communityId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert status code
        response.then().statusCode(200);

        // Validate specific fields in the response body
        Assert.assertNotNull(response.jsonPath().getString("communityId"), "communityId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("communityName"), "communityName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("communityDomain"), "communityDomain should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("type"), "type should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("coverPhoto"), "coverPhoto should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("membersCount"), "membersCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("followersCount"), "followersCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("donationsCount"), "donationsCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("neediesSupportedCount"), "neediesSupportedCount should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isMember"), "isMember should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isFollower"), "isFollower should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("isModerator"), "isModerator should not be null!");
}
    @Test(priority = 2)
    public void ShowCommunityDetailsWithInvalidCommunityTest() throws IOException {

        // Path parameter for community ID
        String communityId = "9999999";

        // Sending GET request to /api/communities/{id}
        RequestSpecification request = given()
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json"); // Add any required headers

        Response response = request.get("/api/communities/" + communityId);

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/" + communityId);
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Assert status code
        response.then().statusCode(404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found", "Error message mismatch!");
    }



    @Test(priority = 3)
    public void howCommunityDetailsWithMissingTokenTest() throws IOException {

        // Path parameter for community ID
        String communityId = "69";

        // Sending GET request to /api/communities/{id}
        RequestSpecification request = given()
                .header("Content-Type", "application/json"); // Add any required headers

        Response response = request.get("/api/communities/" + communityId);

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/" + communityId);
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
