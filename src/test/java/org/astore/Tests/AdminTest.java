package org.astore.Tests;

import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.astore.Requests.Admin;
import org.astore.Utilities.APILogger;
import org.astore.Utilities.ConfigReader;
import org.astore.Utilities.DataGeneratorUtils;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
    static final String EXPECTED_USERNAME = ConfigReader.getUserName();
    static final String EXPECTED_PASSWORD = ConfigReader.getPassword();
    private static final String EXPECTED_FIRST_NAME = ConfigReader.getFirstName();
    private static final String EXPECTED_LAST_NAME = ConfigReader.getLastName();
    private static final String EXPECTED_PHONE_NUM = ConfigReader.getPhone();
    private static final String EXPECTED_EMAIL = ConfigReader.getEmail();

    @BeforeClass
    public void setUp() {
        APILogger.setTest(getTest());
    }

    @Test(description = "Verify that a new user can be successfully registered.", priority = 1)
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


        registerResponse.then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("access_token", Matchers.not(emptyString()));
    }

    @Test(description = "Verify a user cannot register with the same username", dependsOnMethods = "registerAdminTest", priority = 2)
    public static void verifyDuplicateRegistration() {
        JSONObject adminData = new JSONObject();
        adminData.put("firstName", firstName);
        adminData.put("lastName", lastName);
        adminData.put("username", username);
        adminData.put("password", password);
        adminData.put("phone", cellphoneNum);
        adminData.put("email", email);

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        registerResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("name", equalTo("UserExistsError"))
                .body("message", equalTo("User with the specified email has already been registered. Please log in."));
    }

    @Test(description = "Verify a user cannot register without providing a username", priority = 3)
    public static void registerWithoutUsername() {

        JSONObject adminData = new JSONObject();
        firstName = DataGeneratorUtils.generateFirstName();
        adminData.put("firstName", firstName);
        lastName = DataGeneratorUtils.generateLastName();
        adminData.put("lastName", lastName);
        username = DataGeneratorUtils.generateEmailAddress();
        adminData.put("username", "");
        password = DataGeneratorUtils.generatePassword(4, 7);
        adminData.put("password", password);
        cellphoneNum = DataGeneratorUtils.generateSouthAfricanCellphoneNumber();
        adminData.put("phone", cellphoneNum); // Ensure phone is a string
        email = DataGeneratorUtils.generateEmailAddress();
        adminData.put("email", email);

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        registerResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("name", equalTo("MissingUsernameError"))
                .body("message", equalTo("Missing username"));

    }

    @Test(description = "Verify a user cannot register without providing a password.")
    public static void registerWithoutPassword() {
        JSONObject adminData = new JSONObject();
        firstName = DataGeneratorUtils.generateFirstName();
        adminData.put("firstName", firstName);
        lastName = DataGeneratorUtils.generateLastName();
        adminData.put("lastName", lastName);
        username = DataGeneratorUtils.generateEmailAddress();
        adminData.put("username", username);
        cellphoneNum = DataGeneratorUtils.generateSouthAfricanCellphoneNumber();
        adminData.put("phone", cellphoneNum);
        email = DataGeneratorUtils.generateEmailAddress();
        adminData.put("email", email);

        Response registerResponse = Admin.registerAdmin(adminData.toString());


        registerResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("name", equalTo("MissingPasswordError"))
                .body("message", equalTo("Password is required"));
    }

    @Test(description = "Verify a user cannot register without providing an email.")
    public static void registerWithoutEmail() {
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

        Response registerResponse = Admin.registerAdmin(adminData.toString());

        registerResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Email is required."));
    }

//    @Test(description = "Verify that an admin can be logged in")
//    public static void verifySuccessfulLogin() {
//        JSONObject loginData = new JSONObject();
//        loginData.put("username", EXPECTED_USERNAME);
//        loginData.put("password", EXPECTED_PASSWORD);
//
//        Response loginResponse = Admin.loginAdmin(loginData.toString());
//
//        if (loginResponse.getStatusCode() == 401) {
//            System.out.println("Login failed (database cleanup) - running registration and getting new credentials");
//            // Login failed, so run registration and get new credentials
//            registerAdminTest();
//            // Update loginData with the new credentials
//            loginData.put("username", username);
//            loginData.put("password", password);
//            // Retry login
//            loginResponse = Admin.loginAdmin(loginData.toString());
//        }
//
//        // Continue with assertions for successful login
//        accessToken = loginResponse.then().extract().path("access_token");
//        loginResponse.then()
//                .log().status()
//                .log().body()
//                .statusCode(200)
//                .body("success", equalTo(true))
//                .body("access_token" , Matchers.not(emptyString()));
//
//        cookies = loginResponse.detailedCookies();
//    }


    @Test(description = "Verify that an admin cannot be logged in without specifying the username")
    public static void verifyUnsuccessfulLoginWithoutUsername(){
        JSONObject loginData = new JSONObject();
        loginData.put("password", EXPECTED_PASSWORD);

        Response loginResponse = Admin.loginAdmin(loginData.toString());

        loginResponse.then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body(equalTo("Bad Request"));
    }

    @Test(description = "Verify that an admin cannot be logged when using an incorrect username and correct password")
    public static void verifyUnsuccessfulLoginWithIncorrectUsername(){
        JSONObject loginData = new JSONObject();
        loginData.put("username", DataGeneratorUtils.generateEmailAddress());
        loginData.put("password", EXPECTED_PASSWORD);

        Response loginResponse = Admin.loginAdmin(loginData.toString());

        loginResponse.then()
                .log().status()
                .log().body()
                .statusCode(401)
                .body(equalTo("Unauthorized"));
    }

    @Test(description = "Retrieve user details using the access token.")
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
                .body("username", equalTo(EXPECTED_USERNAME))
                .body("firstName", equalTo(EXPECTED_FIRST_NAME))
                .body("lastName", equalTo(EXPECTED_LAST_NAME))
                .body("phone", equalTo(EXPECTED_PHONE_NUM))
                .body("email", equalTo(EXPECTED_EMAIL));
    }

    @Test(description = "Retrieve user details without specifying the access token")
    public static void retrieveUserDetailsWithBlankAccessTkn(){

        //use the access token to retrieve user details
        Response userDetailsResponse = Admin.retrieveUserDetails("");

        userDetailsResponse.then()
                .log().status()
                .log().body()
                .statusCode(401)
                .body("message", equalTo("You are not authenticated for this request"));
    }

    @Test(description = "Retrieve user details using an invalid access token")
    public static void retrieveUserDetailsWithInvalidAccessTkn(){

        //use the access token to retrieve user details
        Response userDetailsResponse = Admin.retrieveUserDetails("eywdjwdjwhdjghdghjgbg23gdjbdjbdwuh1112nmjsbjcnbjcbj");

        userDetailsResponse.then()
                .log().status()
                .log().body()
                .statusCode(401)
                .body("message", equalTo("You are not authenticated for this request"));
    }
}
