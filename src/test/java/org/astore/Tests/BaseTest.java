package org.astore.Tests;

import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;
import static org.astore.Endpoints.EndPoints.BASE_URI;

public class BaseTest {

    private static ExtentTest test;

    @BeforeClass
    public static void initiateBaseURI(){
        baseURI = BASE_URI;
    }

    public static ExtentTest getTest() {
        return test;
    }
}
