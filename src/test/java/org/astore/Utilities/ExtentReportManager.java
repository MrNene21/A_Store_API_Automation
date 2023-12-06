package org.astore.Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager implements ITestListener{

    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public static ExtentTest test;
    String repName;

    public void onStart(ITestContext testContext) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // timestamp
        repName = "Test-Report-" + timeStamp + ".html";
        sparkReporter = new ExtentSparkReporter("./reports/" + repName); // specify location of the report
        sparkReporter.config().setDocumentTitle("A-Store API Automation"); // Title of report
        sparkReporter.config().setReportName("A-Store API"); // name of the report
        sparkReporter.config().setTheme(Theme.DARK);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "A-Store");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("user", "Luyanda");
    }

    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getDescription()); // Use description as test name
        test.assignCategory(result.getMethod().getGroups());

        // Set the current test for logging
        APILogger.setTest(test);
    }

    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, "Test Failed");
        test.log(Status.FAIL, result.getThrowable().getMessage());
    }


    public void onFinish(ITestContext testContext){
        extent.flush();
    }
}
