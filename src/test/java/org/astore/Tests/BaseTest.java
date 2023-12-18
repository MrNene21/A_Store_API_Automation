package org.astore.Tests;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.astore.Requests.Admin;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.swing.*;

import static io.restassured.RestAssured.baseURI;
import static org.astore.Endpoints.EndPoints.BASE_URI;
import static org.astore.Tests.AdminTest.*;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

public class BaseTest {

    private static ExtentTest test;
    protected static String accessToken;


    @BeforeClass
    public static void initiateBaseURI(){
        baseURI = BASE_URI;
        verifySuccessfulLogin();
    }

    public static ExtentTest getTest() {
        return test;
    }

    @Test(description = "Verify that an admin can be logged in.")
    public static void verifySuccessfulLogin() {
        JSONObject loginData = new JSONObject();
        loginData.put("username", EXPECTED_USERNAME);
        loginData.put("password", EXPECTED_PASSWORD);

        Response loginResponse = Admin.loginAdmin(loginData.toString());

        if (loginResponse.getStatusCode() == 401) {
            System.out.println("Login failed (database cleanup) - running registration and getting new credentials");
            // Login failed, so run registration and get new credentials
            registerAdminTest();
            // Update loginData with the new credentials
            loginData.put("username", username);
            loginData.put("password", password);
            // Retry login
            loginResponse = Admin.loginAdmin(loginData.toString());
        }

        // Continue with assertions for successful login
        accessToken = loginResponse.then().extract().path("access_token");
        loginResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("access_token" , Matchers.not(emptyString()));

        cookies = loginResponse.detailedCookies();
    }
}
