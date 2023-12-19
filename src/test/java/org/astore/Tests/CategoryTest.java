package org.astore.Tests;

import io.restassured.response.Response;
import org.astore.Requests.Category;
import org.astore.Utilities.DataGeneratorUtils;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

public class CategoryTest extends BaseTest {

    public static String parentCategory;
    public static String subCategory;
    public static String parentId;

    @Test(description = "Verify that logged in user can create a parent category.")
    public static void createParentCategory() {
        JSONObject categoryData = new JSONObject();
        parentCategory = DataGeneratorUtils.generateCategoryBookGenre();
        categoryData.put("name", parentCategory);

        Response createParentCategoryResponse = Category.createCategory(categoryData.toString(), accessToken);


        int lastIndex = createParentCategoryResponse.jsonPath().getList("_id").size() - 1;
        parentId = createParentCategoryResponse.then().extract().path("_id["+lastIndex+"]");
        createParentCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name[" + lastIndex + "]", endsWith(parentCategory));

    }

    @Test(description = "Verify that logged in user can create a sub category within its parent category.", dependsOnMethods = "createParentCategory")
    public static void createChildCategory() {
        JSONObject categoryData = new JSONObject();
        subCategory = DataGeneratorUtils.generateSubCategoryBookTitle();
        categoryData.put("name", subCategory);
        categoryData.put("parentId", parentId);

        Response createParentCategoryResponse = Category.createCategory(categoryData.toString(), accessToken);

        createParentCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", hasItem(parentCategory))
                .body("children.flatten().findAll { it.parentId == '" + parentId + "' }", everyItem(hasEntry("parentId", parentId)))
                .body("children.flatten().findAll { it.parentId == '" + parentId + "' }.name", hasItem(subCategory));
    }
}
