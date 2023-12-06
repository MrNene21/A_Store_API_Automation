package org.astore.Tests;

import com.beust.ah.A;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.astore.Requests.Admin;
import org.astore.Utilities.APILogger;
import org.astore.Utilities.DataGeneratorUtils;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.util.function.Predicate.not;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

public class AdminTest extends BaseTest {

    public static Cookies cookies;
    public static String firstName;
    public static String lastName;
    public static String username;
    public static String password;
    public static String cellphoneNum;
    public static String email;
    private static String accessToken;

    @BeforeClass
    public void setUp() {
        APILogger.setTest(getTest());
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
        cellphoneNum = DataGeneratorUtils.generateSouthAfricanCellphoneNumber();
        adminData.put("phone", cellphoneNum); // Ensure phone is a string
        email = DataGeneratorUtils.generateEmailAddress();
        adminData.put("email", email);

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        accessToken = registerResponse.then().extract().path("access_token");

        //registerResponse.prettyPrint();

        registerResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("access_token", Matchers.not(emptyString()));
    }

    @Test(description = "Verify a user cannot register with the same username", dependsOnMethods = "registerAdminTest")
    public static void verifyDuplicateRegistration() {
        JSONObject adminData = new JSONObject();
        adminData.put("firstName", firstName);
        adminData.put("lastName", lastName);
        adminData.put("username", username);
        adminData.put("password", password);
        adminData.put("phone", cellphoneNum);

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        //registerResponse.prettyPrint();

        registerResponse.then()
                .log().status()
                .log().body()
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
        adminData.put("phone", cellphoneNum);

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        //registerResponse.prettyPrint();

        registerResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("name", equalTo("MissingUsernameError"))
                .body("message", equalTo("Missing username"));

    }

    @Test(description = "Verify that the admin can be logged in", dependsOnMethods = "registerAdminTest", priority = 1)
    public static void verifySuccessfulLogin(){
        JSONObject loginData = new JSONObject();
        loginData.put("username", username);
        loginData.put("password", password);

        Response loginResponse = Admin.loginAdmin(loginData.toString());

        loginResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("access_token" , Matchers.not(emptyString()));

        cookies = loginResponse.detailedCookies();
    }

    @Test(description = "Verify that an admin cannot be logged in without specifying the username", dependsOnMethods = "registerAdminTest")
    public static void verifyUnsuccessfulLoginWithoutUsername(){
        JSONObject loginData = new JSONObject();
        loginData.put("password", password);

        Response loginResponse = Admin.loginAdmin(loginData.toString());

        loginResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body(equalTo("Bad Request"));
    }

    @Test(description = "Verify that an admin cannot be logged when using an incorrect username and correct password", dependsOnMethods = "registerAdminTest")
    public static void verifyUnsuccessfulLoginWithIncorrectUsername(){
        JSONObject loginData = new JSONObject();
        loginData.put("username", DataGeneratorUtils.generateEmailAddress());
        loginData.put("password", password);

        Response loginResponse = Admin.loginAdmin(loginData.toString());

        loginResponse.then()
                .log().status()
                .log().body()
                .statusCode(401)
                .body(equalTo("Unauthorized"));
    }

    @Test(description = "Retrieve user details using the access token.", dependsOnMethods = "registerAdminTest")
    public static void retrieveUserDetailsTest(){
        //check if the access token if available
        if (accessToken == null){
            throw new RuntimeException("Access token is not available");
        }

        //use the access token to retrieve user details
        Response userDetailsResponse = Admin.retrieveUserDetails(accessToken);

        userDetailsResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("role", equalTo("admin"))
                .body("username", equalTo(username))
                .body("firstName", equalTo(firstName))
                .body("lastName", equalTo(lastName))
                .body("phone", equalTo(cellphoneNum))
                .body("email", equalTo(email));
    }


}
