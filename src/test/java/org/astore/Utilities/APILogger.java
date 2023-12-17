package org.astore.Utilities;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.response.Response;
public class APILogger {
    private static ExtentTest test;

    // Set the current test for logging
    public static void setTest(ExtentTest extentTest) {
        test = extentTest;
    }


    public static void logRequest(String requestPayload) {
        if (test != null) {
            test.info("Request body(Payload) below");
            test.info(MarkupHelper.createCodeBlock(requestPayload, CodeLanguage.JSON));
        }
    }

    // Log response details
    public static void logResponse(Response response) {
        if (test != null) {
            test.info("Response Status Code: " + response.getStatusCode());
            test.info("Find response below");
            test.info(MarkupHelper.createCodeBlock(response.asString(), CodeLanguage.JSON));
        }
    }
}
