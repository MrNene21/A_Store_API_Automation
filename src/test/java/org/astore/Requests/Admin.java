package org.astore.Requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class Admin {


    public static Response restricted() {
        return given().get("/restricted");
    }

    public static Response registerAdmin(String adminData) {
        return given()
                .contentType(ContentType.JSON) // Set the content type of the request
                .body(adminData) // Set the request body as a Map

                .when().post("/admin/register"); // Specify the endpoint for the POST request);

    }

    public static Response loginAdmin(String loginData) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .post("/admin/login");
    }


}
