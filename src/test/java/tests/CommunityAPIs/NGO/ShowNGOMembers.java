package tests.CommunityAPIs.NGO;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

public class ShowNGOMembers {

    private String authToken;
    private String NGOId;
    private String Page;

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
    public void ShowNGOMembersSuccessfullyTest() throws IOException {

        NGOId = "28";
        Page = "1";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .queryParam("page", Page)
                .get("/api/communities/" + NGOId + "/ngo-members");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 200);

        // Validate specific fields in the response body
        Assert.assertNotNull(response.jsonPath().getString("members[0].userId"), "userId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].firstName"), "firstName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].lastName"), "lastName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].avatarUrl"), "avatarUrl should not be null!");
 //       Assert.assertNotNull(response.jsonPath().getString("members[0].numOfSupportedNeedies"), "numOfSupportedNeedies should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].joinedAt"), "joinedAt should not be null!");

        Assert.assertNotNull(response.jsonPath().getString("count"), "count should not be null!");
    }



    @Test(priority = 2)
    public void ShowNGOMembersWithNoPaginationParamterTest() throws IOException {

        NGOId = "28";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .get("/api/communities/" + NGOId + "/ngo-members");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 200);

        // Validate specific fields in the response body
        Assert.assertNotNull(response.jsonPath().getString("members[0].userId"), "userId should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].firstName"), "firstName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].lastName"), "lastName should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].avatarUrl"), "avatarUrl should not be null!");
        //       Assert.assertNotNull(response.jsonPath().getString("members[0].numOfSupportedNeedies"), "numOfSupportedNeedies should not be null!");
        Assert.assertNotNull(response.jsonPath().getString("members[0].joinedAt"), "joinedAt should not be null!");

        Assert.assertNotNull(response.jsonPath().getString("count"), "count should not be null!");

    }

    @Test(priority = 3)
    public void ShowNGOMembersWithInvalidNGOIdTest() throws IOException {

        NGOId = "abc";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .get("/api/communities/" + NGOId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void ShowNGOMembersWithNonExistNGOIdTest() throws IOException {

        NGOId = "999999";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .get("/api/communities/" + NGOId + "/ngo-members");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found.", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void ShowNGOMembersWithMissingNGOIdTest() throws IOException {

        NGOId = " ";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .get("/api/communities/" + NGOId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void ShowNGOMembersWithNoMembersExistForNGOTest() throws IOException {

        NGOId = "33";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .get("/api/communities/" + NGOId + "/ngo-members");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 200);

        // Validate the error message in the response body
        Assert.assertEquals(response.jsonPath().getString("members"), "[]", "members mismatch!");
        Assert.assertEquals(response.jsonPath().getString("count"), "0", "count mismatch!");

    }

    @Test(priority = 7)
    public void ShowNGOMembersWithInvalidPageNumberTest() throws IOException {


        NGOId = "28";
        Page = "abc";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .queryParam("page", Page)
                .get("/api/communities/" + NGOId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Expected number, received nan", "Error message mismatch!");

    }


    @Test(priority = 8)
    public void ShowNGOMembersWithMissingTokenTest() throws IOException {

        NGOId = "28";
        Page = "1";

        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .queryParam("page", Page)
                .get("/api/communities/" + NGOId + "/ngo-members" );

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/" + NGOId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");
    }
}
