package utils;
import config.ConfigReader;
import  io.restassured.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import  org.hamcrest.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RestClient {

    private static final String BASE_URI = ConfigReader.getProperty("baseUri");

    public static Response postRequest(String endpoint, Map<String, Object> body) {
        // Set base URI globally
        RestAssured.baseURI = BASE_URI;

        RequestSpecification request = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body);

        return request.post(endpoint);
    }

    public static Map<String, Object> getTestData(String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/data/TestData.json");

        // Ensure the file exists
        if (!file.exists()) {
            throw new IOException("Test data file not found at path: " + file.getAbsolutePath());
        }

        // Read the JSON file and parse it into a nested Map
        Map<String, Map<String, Object>> data = mapper.readValue(file, new TypeReference<>() {});

        // Return the test data for the specified key
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException("Test data key '" + key + "' not found in JSON file.");
        }

        return data.get(key);
    }
}
