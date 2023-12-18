package org.astore.Tests;

import io.restassured.response.Response;
import org.astore.Requests.Category;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;

public class CategoryTest extends BaseTest {

    @Test(description = "Verify that logged in user can create a parent category.")
    public static void createParentCategory() {
        JSONObject categoryData = new JSONObject();
        categoryData.put("name", "KZN");

        Response createParentCategoryResponse = Category.createCategory(categoryData.toString(), accessToken);

        createParentCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", hasItem("KZN"));
    }
}
