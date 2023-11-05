package org.astore.Tests;

import org.astore.Utilities.ConfigReader;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;
import static org.astore.Endpoints.EndPoints.BASEURI;

public class BaseTest {

    @BeforeClass
    public static void initiateBaseURI(){
            baseURI = BASEURI;
    }
}
