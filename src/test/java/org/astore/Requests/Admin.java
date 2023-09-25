package org.astore.Requests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class Admin {


    public static Response restricted(Cookies cookies) {
        try {
            return given()
                    .cookies(cookies)
                    .get("/restricted");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to access restricted resource: " + e.getMessage());
        }
    }

    public static Response registerAdmin(String adminData) {
        try{
            return given()
                    .contentType(ContentType.JSON) // Set the content type of the request
                    .body(adminData) // Set the request body as a Map

                    .when().post("/admin/register"); // Specify the endpoint for the POST request);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to register admin: " + e.getMessage());
        }

    }

    public static Response loginAdmin(String loginData) {
        try {
            return given()
                    .contentType(ContentType.JSON)
                    .body(loginData)

                    .when().post("/admin/login");

        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to login: " + e.getMessage());
        }

    }


}
