package tests.AuthenticationAPIs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.IOException;
import java.util.Map;

public class RefreshTokenApiTest {

    private String authToken;
    private String refreshToken;

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
        refreshToken = response.jsonPath().getString("refreshToken");
    }

    @Test(priority = 1)
    public  void SuccessTokenRefreshTest() {

        String requestBody = "{\"refreshToken\":\"" + refreshToken +"\"}";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/auth/refresh");

        if (response.getStatusCode() != 201)
        {
            System.out.println("End Point: /api/auth/refresh");
            System.out.println(requestBody);
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Refresh token should not be null!");

    }

    @Test(priority = 2)
    public  void MissingRefreshTokenTest() {

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .post("/api/auth/refresh");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/refresh");
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        Assert.assertEquals(response.jsonPath().getString("message"), "Refresh token is required");

    }

    @Test(priority = 3)
    public  void InvalidRefreshTokenTest() {

        String requestBody = "{\"refreshToken\":\"" + "InvalidToken123" +"\"}";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/auth/refresh");

        if (response.getStatusCode() != 401)
        {
            System.out.println("End Point: /api/auth/refresh");
            System.out.println(requestBody);
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        Assert.assertEquals(response.jsonPath().getString("message"), "Unauthorized");

    }

    @Test(priority = 4)
    public  void MissingTokenFieldRefreshTokenTest() {

        String requestBody = "{\"refreshToken\":\"" + "\"}";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/auth/refresh");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/auth/refresh");
            System.out.println(requestBody);
            response.prettyPrint();
        }

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        Assert.assertEquals(response.jsonPath().getString("message"), "Refresh token is required");

    }

}

