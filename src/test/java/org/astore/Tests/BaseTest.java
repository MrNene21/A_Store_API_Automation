package org.astore.Tests;

import org.astore.Utilities.ConfigReader;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    @BeforeClass
    public static void initiateBaseURI(){
            baseURI = ConfigReader.getBaseURI();
    }
}
