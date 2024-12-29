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

public class ListCommunityMembers {

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
    public void ListCommunityMembersSuccessfullyTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Add query parameters
        request.queryParam("page", 1);

        // Make a GET request to the endpoint and store the response
        String memberId = "69";
        Response response = request.get("/api/communities/"+ memberId +"/members");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/"+ memberId +"/members");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

        // Validate specific fields in the response body
        Assert.assertNotNull(response.jsonPath().getString("members[0].userId"), "userId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].firstName"), "firstName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].lastName"), "lastName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].avatarUrl"), "avatarUrl should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].numOfSupportedNeedies"), "numOfSupportedNeedies should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].joinedAt"), "joinedAt should not be null!");

        Assert.assertNotNull(response.jsonPath().getString("count"), "count should not be null!");
    }



    @Test(priority = 2)
    public void ListCommunityMembersWithNoPaginationParamterTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Make a GET request to the endpoint and store the response
        String memberId = "69";
        Response response = request.get("/api/communities/"+ memberId +"/members");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/"+ memberId +"/members");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

        // Validate specific fields in the response body
        Assert.assertNotNull(response.jsonPath().getString("members[0].userId"), "userId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].firstName"), "firstName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].lastName"), "lastName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].avatarUrl"), "avatarUrl should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].numOfSupportedNeedies"), "numOfSupportedNeedies should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].joinedAt"), "joinedAt should not be null!");

        Assert.assertNotNull(response.jsonPath().getString("count"), "count should not be null!");

    }

    @Test(priority = 3)
    public void ListCommunityMembersWithInvalidCommunityIdTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);



        // Make a GET request to the endpoint and store the response
        String memberId = "9999";
        Response response = request.get("/api/communities/"+ memberId +"/members");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ memberId +"/members");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found.", "Error message mismatch!");

    }

    @Test(priority = 4)
    public void ListCommunityMembersWithNoMembersExistForCommunityTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);


        // Make a GET request to the endpoint and store the response
        String memberId = "85";
        Response response = request.get("/api/communities/"+ memberId +"/members");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/"+ memberId +"/members");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(200);

        // Validate the error message in the response body
        Assert.assertEquals(response.jsonPath().getString("members"), "[]", "members mismatch!");
        Assert.assertEquals(response.jsonPath().getString("count"), "0", "count mismatch!");

    }

    @Test(priority = 5)
    public void FindMyommunitiesWithInvalidPageNumberTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Add query parameters
        request.queryParam("page", "one");

        // Make a GET request to the endpoint and store the response
        String memberId = "34";
        Response response = request.get("/api/communities/"+ memberId +"/members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ memberId +"/members");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Expected number, received nan", "Error message mismatch!");

    }

    @Test(priority = 5)
    public void FindAllCommunitiesWithInvalidMemberIdTest() throws IOException {

        RequestSpecification request = given();

        // Add the Authorization header with the token
        request.header("Authorization", "Bearer " + authToken);

        // Add query parameters
        request.queryParam("page", 1);

        // Make a GET request to the endpoint and store the response
        String memberId = "-1";
        Response response = request.get("/api/communities/"+ memberId +"/members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ memberId +"/members");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");

    }

    @Test(priority = 6)
    public void FindAllCommunitiesWithMissingTokenTest() throws IOException {

        RequestSpecification request = given();

        // Add query parameters
        request.queryParam("page", 1);

        // Make a GET request to the endpoint and store the response
        String memberId = "34";
        Response response = request.get("/api/communities/"+ memberId +"/members");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/"+ memberId +"/members");
            System.out.println("Response Body:");
            response.prettyPrint();
        }

        // Validate the response status code
        response.then().statusCode(401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Access token is required", "Error message mismatch!");
    }
}
