package com.orangehrm.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    protected static final String BASE_URL =
            "https://opensource-demo.orangehrmlive.com";


    // =========================
    // SETUP
    // =========================

    @BeforeMethod
    public void setUp() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        // Uncomment for CI / headless execution:
        // options.addArguments("--headless=new");

        driver = new ChromeDriver(options);

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(5));

        driver.get(
                BASE_URL + "/web/index.php/auth/login"
        );

        takeScreenshot("01 - Login Page Loaded");
    }


    // =========================
    // TEARDOWN
    // =========================

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (driver != null) {

            /*
             * Take screenshot at the end of every test.
             * This makes the final state visible in Allure.
             */

            if (result.getStatus() == ITestResult.FAILURE) {

                takeScreenshot("FAILED - Final State");

            } else {

                takeScreenshot("PASSED - Final State");
            }

            driver.quit();
        }
    }


    // =========================
    // SCREENSHOT
    // =========================

    public void takeScreenshot(String name) {

        try {

            if (driver == null) {
                return;
            }

            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );

        } catch (Exception e) {

            System.out.println(
                    "Unable to take screenshot: "
                            + e.getMessage()
            );
        }
    }


    // =========================
    // SCREENSHOT HELPER
    // =========================

    protected void captureStepScreenshot(String stepName) {

        takeScreenshot(stepName);
    }
}