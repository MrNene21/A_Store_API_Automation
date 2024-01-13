package org.astore.Requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.astore.Utilities.APILogger;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.astore.Endpoints.EndPoints.PRODUCT_CREATE;

public class Product {

    public static Response createProduct(String productData, String accessToken) {
        try {
            APILogger.logRequest(productData);

            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "token " + accessToken)
                    .body(productData)

                    .when().post(PRODUCT_CREATE);

            APILogger.logResponse(response);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create product: " + e.getMessage());
        }
    }

}
