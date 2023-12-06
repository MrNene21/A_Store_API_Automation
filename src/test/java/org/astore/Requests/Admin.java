package org.astore.Requests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.astore.Utilities.APILogger;

import static io.restassured.RestAssured.given;
import static org.astore.Endpoints.EndPoints.*;

public class Admin {


    public static Response registerAdmin(String adminData) {
        try{
            // Log the request payload
            APILogger.logRequest(adminData);

            Response response = given()
                    .contentType(ContentType.JSON) // Set the content type of the request
                    .body(adminData) // Set the request body as a Map

                    .when().post(ADMIN_REGSISTER); // Specify the endpoint for the POST request);

            // Log the response details
            APILogger.logResponse(response);

            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to register admin: " + e.getMessage());
        }
    }

    public static Response loginAdmin(String loginData) {
        try {
            APILogger.logRequest(loginData);

            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(loginData)

                    .when().post(ADMIN_LOGIN);

            APILogger.logResponse(response);
            return response;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to login: " + e.getMessage());
        }
    }

    public static Response retrieveUserDetails(String accessToken){
        try {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization","Bearer " + accessToken)
                    .when().get(ADMIN_USER_DETAILS);

            APILogger.logResponse(response);

            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve user details: " + e.getMessage());
        }
    }


}
