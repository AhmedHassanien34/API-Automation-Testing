package tests.AuthenticationAPIs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class GetNationalIdFromValify {

    private String authToken;

    @BeforeClass
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("NonVerifiedLogin");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");
    }


    @Test(priority = 1)
    public void GetNationalIdFromvalifySuccessfullyTest() throws IOException {

        Map<String, Object> requestBody = RestClient.getTestData("NationalIdVerification");

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch("/api/auth/national-id");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/auth/national-id");
            System.out.println("Request Payload: NationalIdVerification");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validating response
        Assert.assertEquals(response.getStatusCode(), 200);

    }
}
