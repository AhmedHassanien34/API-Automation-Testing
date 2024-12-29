package tests.CommunityAPIs.NGO;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import tests.CommunityAPIs.Community.CreateNewCommunity;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class CreateNewNGOCommunity {

    private String authToken;

    private String VerifyingNGOId;
    private String CreatingNGOId;

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
    public void CreateNewVerifyingNGOSuccessfullyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidVerifyingCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: ValidVerifyingCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

    }

    @Test(priority = 2)
    public void CreateNewCreatingNGOSuccessfullyTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidCreatingCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: ValidCreatingCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

    }

    @Test(priority = 3)
    public void CreateNewCreatingNGOWithMissingCoverPhotoURLTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("MissingCoverPhotoURLCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: MissingCoverPhotoURLCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Cover photo is required", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void CreateNewCreatingNGOWithInvalidCoverPhotoURLFormatTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("InvalidCoverPhotoURLFormatCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: InvalidCoverPhotoURLFormatCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid url", "Error message mismatch!");
    }

    @Test(priority = 5)
    public void CreateNewCreatingNGOWithUnsupportedCommunityTypeTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("UnsupportedCommunityTypeCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: UnsupportedCommunityTypeCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Invalid enum value. Expected 1 | 2, received '5'", "Error message mismatch!");
    }

    @Test(priority = 6)
    public void CreateNewCreatingNGOWithMissingCommunityNameTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("MissingCommunityNameCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: MissingCommunityNameCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Required", "Error message mismatch!");
    }

    @Test(priority = 7)
    public void CreateNewCreatingNGOWithEmptyCommunityNameTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("EmptyCommunityNameCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: EmptyCommunityNameCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Name is required", "Error message mismatch!");
    }

    @Test(priority = 8)
    public void CreateNewCreatingNGOWithDuplicateCommunityNameTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("DuplicateCommunityNameCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: DuplicateCommunityNameCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "A community already exists with the same name", "Error message mismatch!");
    }

    @Test(priority = 9)
    public void CreateNewVerifyingWithMissingTokenTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("ValidVerifyingCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: ValidVerifyingCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Unauthorized", "Error message mismatch!");

    }

    @Test(priority = 10)
    public void CreateNewVerifyingWithLongCommunityNameTest() throws IOException {
        // Get test data for valid registration
        Map<String, Object> requestBody = RestClient.getTestData("LongCommunityNameCreationNGO");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/communities/ngo");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/communities/ngo");
            System.out.println("Request Payload: LongCommunityNameCreationNGO");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage, "Name must be at most 50 characters long", "Error message mismatch!");
    }


    @AfterClass
    public void DeleteCreatingNGOSuccessfullyTest() throws IOException {

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/communities/" + VerifyingNGOId );

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/" + CreatingNGOId );
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        // Send POST request
        Response response2 = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .delete ("/api/communities/" + CreatingNGOId );

        if (response2.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/communities/" + CreatingNGOId );
            System.out.println("Request Body:");
            response2.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response2.getStatusCode(), 200, "Status code mismatch!");


    }

}
