package tests.CommunityAPIs.Posts;

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

public class ShowCommunityPost {

    private String CommunityPosts;
    private String PageSize;
    private String page;


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
        public void ShowCommunityPostSuccessfullyTest() throws IOException {

            CommunityPosts = "28";
            PageSize = "10";
            page = "1";

            // Send POST request
            Response response = RestAssured.given()
                    .header("Authorization", "Bearer " + authToken)
                    .contentType(ContentType.JSON)
                    .queryParam("communityId", CommunityPosts)
                    .queryParam("pageSize", PageSize)
                    .queryParam("page", page)
                    .get("/api/posts/");

            if (response.getStatusCode() != 200)
            {
                System.out.println("End Point: /api/posts/");
                System.out.println("Request Body:");
                response.prettyPrint();
            }

            // Validate response
            Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

            // Check that id, communityId, userId, isModeratorPost, isPinned and createdAt are not null
            Assert.assertNotNull(response.jsonPath().getString("posts[0].postId"), "id postId not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].isModeratorPost"), "isModeratorPost should not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].isPinned"), "isPinned should not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].postContent"), "postContent should not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].postDate"), "postDate should not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].userId"), "userId should not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].userFirstName"), "userFirstName not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].userLastName"), "userLastName should not be null!");
            Assert.assertNotNull(response.jsonPath().getString("posts[0].userPicture"), "userPicture should not be null!");

        }

        @Test(priority = 2)
        public void ShowCommunityPostWithInvalidPostId() throws IOException {

            CommunityPosts = "abc";
            PageSize = "10";
            page = "1";

            // Send POST request
            Response response = RestAssured.given()
                    .header("Authorization", "Bearer " + authToken)
                    .contentType(ContentType.JSON)
                    .queryParam("communityId", CommunityPosts)
                    .queryParam("pageSize", PageSize)
                    .queryParam("page", page)
                    .get("/api/posts/");

            if (response.getStatusCode() != 400)
            {
                System.out.println("End Point: /api/posts/");
                System.out.println("Request Body:");
                response.prettyPrint();
            }

            // Validate response
            Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");


            // Validate the error message in the response body
            String errorMessage1 = response.jsonPath().getString("errors[0].message");
            Assert.assertEquals(errorMessage1, "Expected number, received nan", "Error message mismatch!");
        }

        @Test(priority = 3)
        public void ShowCommunityPostWithNonExistPostId() throws IOException {

            CommunityPosts = "99999";
            PageSize = "10";
            page = "1";

            // Send POST request
            Response response = RestAssured.given()
                    .header("Authorization", "Bearer " + authToken)
                    .contentType(ContentType.JSON)
                    .queryParam("communityId", CommunityPosts)
                    .queryParam("pageSize", PageSize)
                    .queryParam("page", page)
                    .get("/api/posts/");

            if (response.getStatusCode() != 404)
            {
                System.out.println("End Point: /api/posts/");
                System.out.println("Request Body:");
                response.prettyPrint();
            }

            // Validate response
            Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch!");

            // Validate the error message in the response body
            String errorMessage1 = response.jsonPath().getString("message");
            Assert.assertEquals(errorMessage1, "Community not found", "Error message mismatch!");
        }

        @Test(priority = 4)
        public void ShowCommunityPostWithMissingTokenTest() throws IOException {

            CommunityPosts = "28";
            PageSize = "10";
            page = "1";

            // Send POST request
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .queryParam("communityId", CommunityPosts)
                    .queryParam("pageSize", PageSize)
                    .queryParam("page", page)
                    .get("/api/posts/");

            if (response.getStatusCode() != 401)
            {
                System.out.println("End Point: /api/posts/");
                System.out.println("Request Body:");
                response.prettyPrint();
            }

            // Validate response
            Assert.assertEquals(response.getStatusCode(), 401, "Status code mismatch!");

        }

    @Test(priority = 5)
    public void ShowCommunityPostWithNoPostsAreAvailableTest() throws IOException {

        CommunityPosts = "1";
        PageSize = "10";
        page = "1";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("communityId", CommunityPosts)
                .queryParam("pageSize", PageSize)
                .queryParam("page", page)
                .get("/api/posts/");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/posts/");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");



    }

    @Test(priority = 6)
    public void ShowCommunityPostWithZeroPageSizeTest() throws IOException {

        CommunityPosts = "28";
        PageSize = "0";
        page = "1";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("communityId", CommunityPosts)
                .queryParam("pageSize", PageSize)
                .queryParam("page", page)
                .get("/api/posts/");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage1, "Number must be greater than 0", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage2, "pageSize", "Error message mismatch!");

    }

    @Test(priority = 7)
    public void ShowCommunityPostWithNonNumericPageAndPageSizeTest() throws IOException {

        CommunityPosts = "28";
        PageSize = "abc";
        page = "xyz";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("communityId", CommunityPosts)
                .queryParam("pageSize", PageSize)
                .queryParam("page", page)
                .get("/api/posts/");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage1, "pageSize", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Expected number, received nan", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage3 = response.jsonPath().getString("errors[1].path[0]");
        Assert.assertEquals(errorMessage3, "page", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage4 = response.jsonPath().getString("errors[1].message");
        Assert.assertEquals(errorMessage4, "Expected number, received nan", "Error message mismatch!");
    }

    @Test(priority = 8)
    public void ShowCommunityPostWithPageNumberExceedsAvailablePagesTest() throws IOException {

        CommunityPosts = "28";
        PageSize = "10";
        page = "100";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("communityId", CommunityPosts)
                .queryParam("pageSize", PageSize)
                .queryParam("page", page)
                .get("/api/posts/");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/posts/");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(priority = 9)
    public void ShowCommunityPostWithPageSizeNumberExceedsAllowedTheMaximumNumberTest() throws IOException {

        CommunityPosts = "1";
        PageSize = "1000";
        page = "1";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("communityId", CommunityPosts)
                .queryParam("pageSize", PageSize)
                .queryParam("page", page)
                .get("/api/posts/");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 400, "Status code mismatch!");

        // Validate the error message in the response body
        String errorMessage1 = response.jsonPath().getString("errors[0].path[0]");
        Assert.assertEquals(errorMessage1, "pageSize", "Error message mismatch!");

        // Validate the error message in the response body
        String errorMessage2 = response.jsonPath().getString("errors[0].message");
        Assert.assertEquals(errorMessage2, "Page size cannot exceed 20", "Error message mismatch!");
    }

    @Test(priority = 10)
    public void ShowCommunityPostWithNoPageSizeNumberAndPageNumberTest() throws IOException {

        CommunityPosts = "1";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("communityId", CommunityPosts)
                .get("/api/posts/");

        if (response.getStatusCode() != 200)
        {
            System.out.println("End Point: /api/posts/");
            System.out.println("Request Body:");
            response.prettyPrint();
        }

        // Validate response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");

        Assert.assertNotNull(response.jsonPath().getString("count"), "count should not be null!");
    }

    @Test(priority = 11)
    public void ShowCommunityPostWithMissingCommunityPostsTest() throws IOException {

        PageSize = "1";
        page = "1";

        // Send POST request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("pageSize", PageSize)
                .queryParam("page", page)
                .get("/api/posts/");

        if (response.getStatusCode() != 400)
        {
            System.out.println("End Point: /api/posts/");
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

    }
}

