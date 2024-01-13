package org.astore.Requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.astore.Tests.BaseTest;
import org.astore.Utilities.APILogger;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.astore.Endpoints.EndPoints.*;

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

    public static Response deleteCategory(String categoryData, String accessToken){
        try {
            APILogger.logRequest(categoryData);
            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization","Bearer " + accessToken)
                    .body(categoryData)

                    .when().delete(CATEGORY_DELETE);
            APILogger.logResponse(response);

            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to delete category: " + e.getMessage());
        }
    }

    public static Response retrieveCategories(){
        try {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when().get(CATEGORY_RETURN);

            APILogger.logResponse(response);

            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve categories: " + e.getMessage());
        }
    }
}
