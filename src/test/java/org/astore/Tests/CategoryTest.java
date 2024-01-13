package org.astore.Tests;

import io.restassured.response.Response;
import org.astore.Requests.Category;
import org.astore.Utilities.DataGeneratorUtils;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasEntry;

public class CategoryTest extends BaseTest {
    public static String parentCategory;
    public static String subCategory;
    public static String parentId;
    public static String characters;

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

    @Test(description = "Verify that a category with less than 3 characters cannot be created")
    public static void createEmptyCategoryName() {
        JSONObject categoryData = new JSONObject();
        String categoryName = "";
        categoryData.put("name",categoryName);
        String expectedMsg = "Path `name` (`" + categoryName + "`) is shorter than the minimum allowed length (3).";

        Response createParentCategoryResponse = Category.createCategory(categoryData.toString(), accessToken);

        createParentCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("errors[0]", equalTo(expectedMsg));
    }

    @Test(description = "Verify that a category with less than 3 characters cannot be created")
    public static void verifyLessThanThreeCategoryName() {
        JSONObject categoryData = new JSONObject();
        characters = DataGeneratorUtils.generateAlphaNumericCharacters(0,2);
        categoryData.put("name", characters);
        String expectedMsg = "Path `name` (`" + characters + "`) is shorter than the minimum allowed length (3).";

        Response createParentCategoryResponse = Category.createCategory(categoryData.toString(), accessToken);

        createParentCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("errors[0]", equalTo(expectedMsg));
    }

    @Test(description = "Verify that a category with more than 80 characters cannot be created")
    public static void verifyMoreThanEightyCategoryName() {
        JSONObject categoryData = new JSONObject();
        characters = DataGeneratorUtils.generateAlphaNumericCharacters(80,85);
        categoryData.put("name", characters);
        String expectedMsg = "Path `name` (`" + characters + "`) is longer than the maximum allowed length (80).";

        Response createParentCategoryResponse = Category.createCategory(categoryData.toString(), accessToken);

        createParentCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("errors[0]", equalTo(expectedMsg));
    }

    @Test(description = "Verify a user can create a sub category within its parent category.", dependsOnMethods = "createParentCategory")
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

    @Test(description = "Verify that a user cannot create a category while having an invalid access token.")
    public static void createParentCategoryWithInvalidAccessToken() {
        JSONObject categoryData = new JSONObject();
        parentCategory = DataGeneratorUtils.generateCategoryBookGenre();
        categoryData.put("name", parentCategory);

        Response createParentCategoryResponse = Category.createCategory(categoryData.toString(), "InvalidAccessToken1245");

        createParentCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(401)
                .body("message", equalTo("You are not authenticated for this request"));
    }

    @Test(description = "Verify that a single parent category can be deleted after its creation", dependsOnMethods = "createParentCategory")
    public static void deleteParentCategory(){
        JSONArray categoryArray = new JSONArray();
        JSONObject categoryData = new JSONObject();
        categoryData.put("_id", parentId);
        categoryArray.put(categoryData);

        Response deleteCategoryResponse = Category.deleteCategory(categoryArray.toString(), accessToken);

        deleteCategoryResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("_parentId", not(parentId))
                .body("name", not(parentCategory));
    }

    @Test(description = "Verify that the created categories can be returned", dependsOnMethods = "createParentCategory")
    public static void returnCategories(){

        Response retrieveCategoriesResponse = Category.retrieveCategories();

        retrieveCategoriesResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .contentType("application/json")
                .body("find {it.containsKey('name')}", Matchers.notNullValue());
    }
}
