package org.astore.Requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.astore.Tests.BaseTest;
import org.astore.Utilities.APILogger;

import static io.restassured.RestAssured.given;
import static org.astore.Endpoints.EndPoints.CATEGORY_CREATE;

public class Category {

    public static Response createCategory(String categoryData, String accessToken){
        try{
            APILogger.logRequest(categoryData);

            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization","Bearer " + accessToken)
                    .body(categoryData)

                    .when().post(CATEGORY_CREATE);

            APILogger.logResponse(response);

            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to create category: " + e.getMessage());
        }
    }
}
