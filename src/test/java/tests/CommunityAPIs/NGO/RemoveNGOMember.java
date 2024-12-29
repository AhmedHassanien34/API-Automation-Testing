package tests.CommunityAPIs.NGO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RemoveNGOMember {

    private String authToken;

    @BeforeClass
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("ModeratorLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");  // Adjust "token" based on your JSON structure
    }


    @Test(priority = 1)
    public void RemoveNGOMemberToNGOCommunitySuccessfulTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "192";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParams("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 200);

    }



    @Test(priority = 2)
    public void RemoveNGOMemberToNonNGOCommunityTest(){



        // Set the Community ID and User ID as path and query parameters
        String communityId = "1";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community is not an NGO", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void RemoveNGOMemberToNonExistNGOCommunityTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "99999";
        String userId = "31";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Community not found.", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void RemoveNGOMemberToInvalidNGOCommunityTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "abc";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void RemoveNGOMemberToNGOWithInvalidUserIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";
        String userId = "xyz";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
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
    public void RemoveNGOMemberToNGOWithMissingTokenTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .contentType(ContentType.JSON)
                .queryParam("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 401);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");
    }

    @Test(priority = 7)
    public void RemoveNGOMemberToNGOWithWhoIsAlreadyDeletedTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "433";
        String userId = "192";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 404)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 404);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "User is not a member of the community", "Error message mismatch!");
    }

    @Test(priority = 8)
    public void RemoveNGOMemberToNGOWithMissingCommunityIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = " ";
        String userId = "96";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("memberUserId", userId)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Parameter path \"id\" must be valid number.", "Error message mismatch!");
    }

    @Test(priority = 9)
    public void RemoveNGOMemberrToNGOWithMissingUserIdTest(){

        // Set the Community ID and User ID as path and query parameters
        String communityId = "28";

        // Sending POST request
        Response response = given()
                .header("Content-Type", "application/json") // Add necessary headers
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete("/api/communities/"+ communityId + "/ngo-members");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/"+ communityId + "/ngo-members");
            System.out.println("Request Body:");
            response.prettyPrint();
        }


        // Validating response
        Assert.assertEquals(response.getStatusCode(), 400);

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Required", "Error message mismatch!");
    }
}
