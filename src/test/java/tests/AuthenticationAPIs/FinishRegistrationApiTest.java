package tests.AuthenticationAPIs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RestClient;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FinishRegistrationApiTest {

    private String authToken;

    @BeforeTest
    public void ValidEmailandValidPasswordLogin() throws IOException {
        Map<String, Object> requestBody = RestClient.getTestData("VerifiedGiversAccount");

        // Send POST request to register endpoint
        Response response = RestClient.postRequest("/api/auth/login", requestBody);

        // Assert the response status code is 201
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // Check that accessToken is not null
        Assert.assertNotNull(response.jsonPath().getString("accessToken"), "Access token should not be null!");

        authToken = response.jsonPath().getString("accessToken");  // Adjust "token" based on your JSON structure
    }


    @Test(priority = 1)
    public void FinishRegistrationTest() {

        // Path to your files
        File frontPhoto = new File("images/Front_ID.png");
        File backPhoto = new File("images/Back_ID.png");

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdNumber", "30005010102916") // Text field
                .multiPart("nationalIdFrontPhoto", frontPhoto, "image/png")   // File field
                .multiPart("nationalIdBackPhoto", backPhoto, "image/png")     // File field
                .post("/api/auth/finish-registration");

        System.out.println("Response Body: " + response.getBody().asString());
        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201 but received " + response.getStatusCode());


    }

    @Test(priority = 2)
    public void FinishRegistrationMissingNationalIDNumberTest() {
        // Base URI
        RestAssured.baseURI = "https://stg.givers-app.com";

        // Path to your files
        File frontPhoto = new File("images/Front_ID.png");
        File backPhoto = new File("images/Back_ID.png");

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdFrontPhoto", frontPhoto, "image/png")   // File field
                .multiPart("nationalIdBackPhoto", backPhoto, "image/png")     // File field
                .post("/api/auth/finish-registration");

        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 but received " + response.getStatusCode());

        String errorMessage = response.jsonPath().getString("errors.message");
        Assert.assertEquals(errorMessage, "[National ID number is required]");
    }

    @Test(priority = 3)
    public void FinishRegistrationMissingNationalIDFrontPhotoTest() {
        // Base URI
        RestAssured.baseURI = "https://stg.givers-app.com";

        // Path to your files
        File backPhoto = new File("images/Back_ID.png");

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdNumber", "30005010102916") // Text field
                .multiPart("nationalIdBackPhoto", backPhoto, "image/png")     // File field
                .post("/api/auth/finish-registration");

        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 but received " + response.getStatusCode());

        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Both the front and back photos of national ID must be provided");
    }

    @Test(priority = 4)
    public void FinishRegistrationMissingNationalIDBackPhotoTest() {
        // Base URI
        RestAssured.baseURI = "https://stg.givers-app.com";

        // Path to your files
        File frontPhoto = new File("images/Front_ID.png");

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdNumber", "30005010102916") // Text field
                .multiPart("nationalIdFrontPhoto", frontPhoto, "image/png")   // File field
                .post("/api/auth/finish-registration");

        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 but received " + response.getStatusCode());

        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Both the front and back photos of national ID must be provided");
    }

    @Test(priority = 5)
    public void FinishRegistrationInvalidNationalIDNumberTest() {
        // Base URI
        RestAssured.baseURI = "https://stg.givers-app.com";

        // Path to your files
        File frontPhoto = new File("images/Front_ID.png");
        File backPhoto = new File("images/Back_ID.png");

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdNumber", "30005010102aaa") // Text field
                .multiPart("nationalIdFrontPhoto", frontPhoto, "image/png")   // File field
                .multiPart("nationalIdBackPhoto", backPhoto, "image/png")     // File field
                .post("/api/auth/finish-registration");

        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 but received " + response.getStatusCode());

        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "National ID number must contain only digits");

        String errorMessage2 = response.jsonPath().getString("errors[1].message");
        Assert.assertEquals(errorMessage2, "National ID number must be a positive integer");
    }

    @Test(priority = 6)
    public void FinishRegistrationInvalidFrontPhotoFormatTest() {
        // Base URI
        RestAssured.baseURI = "https://stg.givers-app.com";

        // Path to your files
        File frontPhoto = new File("files/file.txt");
        File backPhoto = new File("images/Back_ID.png");

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdNumber", "30005010102aaa") // Text field
                .multiPart("nationalIdFrontPhoto", frontPhoto)   // File field
                .multiPart("nationalIdBackPhoto", backPhoto, "image/png")     // File field
                .post("/api/auth/finish-registration");

        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 but received " + response.getStatusCode());

        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Only image files (JPEG, PNG, GIF, BMP) and PDFs are allowed!");
    }

    @Test(priority = 7)
    public void FinishRegistrationInvalidBackPhotoFormatTest() {
        // Base URI
        RestAssured.baseURI = "https://stg.givers-app.com";

        // Path to your files
        File frontPhoto = new File("images/Front_ID.png");
        File backPhoto = new File("files/file.txt");

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdNumber", "30005010102aaa") // Text field
                .multiPart("nationalIdFrontPhoto", frontPhoto , "image/png")   // File field
                .multiPart("nationalIdBackPhoto", backPhoto)     // File field
                .post("/api/auth/finish-registration");

        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 but received " + response.getStatusCode());

        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Only image files (JPEG, PNG, GIF, BMP) and PDFs are allowed!");
    }

    @Test(priority = 8)
    public void FinishRegistrationEmptyPhotoFieldsTest() {
        // Base URI
        RestAssured.baseURI = "https://stg.givers-app.com";

        // Sending POST request with form data
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("nationalIdNumber", "30005010102916") // Text field
                .post("/api/auth/finish-registration");

        // Assert to check status code if needed
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 but received " + response.getStatusCode());

        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Both the front and back photos of national ID must be provided");
    }

}
