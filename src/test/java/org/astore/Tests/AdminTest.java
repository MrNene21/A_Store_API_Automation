package org.astore.Tests;

import io.restassured.response.Response;
import org.astore.Requests.Admin;

import org.astore.Utilities.DataGeneratorUtils;
import org.json.JSONObject;
import org.testng.annotations.Test;


import static org.hamcrest.Matchers.equalTo;

public class AdminTest extends BaseTest {

    public static String firstName;
    public static String lastName;
    public static String username;
    public static String password;

    @Test(description = "Verify that the restricted resource can be accessed", dependsOnMethods = "verifySuccessfulLogin")
    public static void verifyRestrictedResource() {
        Response restrictedResponse = Admin.restricted();
        System.out.println(restrictedResponse.getStatusCode());
        restrictedResponse.prettyPrint();
        restrictedResponse.then()
                .statusCode(200)
                .body("message", equalTo("access to restricted resource confirmed"));
    }

    @Test(description = "Verify that a new user can be successfully registered.")
    public static void registerAdminTest() {
        JSONObject adminData = new JSONObject();
        firstName = DataGeneratorUtils.generateFirstName();
        adminData.put("firstName", firstName);
        lastName = DataGeneratorUtils.generateLastName();
        adminData.put("lastName", lastName);
        username = DataGeneratorUtils.generateEmailAddress();
        adminData.put("username", username);
        password = DataGeneratorUtils.generatePassword(4, 7);
        adminData.put("password", password);
        adminData.put("phone", "791369397"); // Ensure phone is a string

        System.out.println(username);
        System.out.println(password);

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        registerResponse.prettyPrint();

        registerResponse.then()
                .statusCode(200)
                .body("role", equalTo("admin"))
                .body("firstName", equalTo(firstName))
                .body("lastName", equalTo(lastName));

    }

    @Test(description = "Verify a user cannot register with the same username", dependsOnMethods = "registerAdminTest")
    public static void verifyDuplicateRegistration() {
        JSONObject adminData = new JSONObject();
        adminData.put("firstName", firstName);
        adminData.put("lastName", lastName);
        adminData.put("username", username);
        adminData.put("password", password);
        adminData.put("phone", "0791368397");

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        registerResponse.prettyPrint();

        registerResponse.then()
                .statusCode(400)
                .body("name", equalTo("UserExistsError"))
                .body("message", equalTo("A user with the given username is already registered"));
    }

    @Test(description = "Verify a user cannot register without providing a username")
    public static void verifyEmptyUsername() {

        JSONObject adminData = new JSONObject();
        firstName = DataGeneratorUtils.generateFirstName();
        adminData.put("firstName", firstName);
        lastName = DataGeneratorUtils.generateLastName();
        adminData.put("lastName", lastName);
        adminData.put("username", "");
        adminData.put("password", password);
        adminData.put("phone", "0791369397");

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        registerResponse.prettyPrint();

        registerResponse.then()
                .statusCode(400)
                .body("name", equalTo("MissingUsernameError"))
                .body("message", equalTo("Missing username"));

    }

    @Test(description = "Verify that the admin can be logged in", dependsOnMethods = "registerAdminTest")
    public static void verifySuccessfulLogin(){
        JSONObject loginData = new JSONObject();
        loginData.put("username", username);
        loginData.put("password", password);

        Response loginResponse = Admin.loginAdmin(loginData.toString());

        loginResponse.then()
                .log().status()
                .log().body()
                .assertThat().statusCode(200)
                .assertThat().body("role", equalTo("admin"));
    }

}
