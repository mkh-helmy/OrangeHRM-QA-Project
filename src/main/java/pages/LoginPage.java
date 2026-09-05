package com.orangehrm.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    // =========================
    // LOCATORS
    // =========================

    private final By usernameInput =
            By.name("username");

    private final By passwordInput =
            By.name("password");

    private final By loginButton =
            By.cssSelector("button[type='submit']");

    private final By errorAlert =
            By.xpath("//p[contains(@class,'alert-content-text')]");

    private final By dashboardHeader =
            By.xpath("//h6[normalize-space()='Dashboard']");

    private final By forgotPasswordLink =
            By.xpath("//p[contains(@class,'orangehrm-login-forgot-header')]");

    private final By resetUsernameInput =
            By.name("username");

    private final By resetButton =
            By.cssSelector("button[type='submit']");

    private final By resetSuccessMessage =
            By.xpath(
                    "//h6[normalize-space()='Reset Password link sent successfully']"
            );

    private final By gatewayTimeoutText =
            By.xpath(
                    "//*[contains(normalize-space(),'504 Gateway Time-out') " +
                            "or contains(normalize-space(),'504 Gateway Timeout') " +
                            "or normalize-space()='504']"
            );


    // =========================
    // LOGIN
    // =========================

    @Step("Enter username: {username}")
    public void enterUsername(String username) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameInput)
        ).clear();

        driver.findElement(usernameInput)
                .sendKeys(username);

        takeScreenshot("Enter Username");
    }


    @Step("Enter password")
    public void enterPassword(String password) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordInput)
        ).clear();

        driver.findElement(passwordInput)
                .sendKeys(password);

        takeScreenshot("Enter Password");
    }


    @Step("Click login button")
    public void clickLogin() {

        wait.until(
                ExpectedConditions.elementToBeClickable(loginButton)
        ).click();

        takeScreenshot("Click Login Button");
    }


    @Step("Perform login with username: {username}")
    public void login(String username, String password) {

        enterUsername(username);
        enterPassword(password);
        clickLogin();

        takeScreenshot("Login Completed");
    }


    // =========================
    // LOGIN VERIFICATION
    // =========================

    @Step("Verify dashboard is displayed")
    public boolean isDashboardDisplayed() {

        try {

            boolean displayed = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            dashboardHeader
                    )
            ).isDisplayed();

            takeScreenshot("Dashboard Verification");

            return displayed;

        } catch (Exception e) {

            takeScreenshot("Dashboard Verification - Failed");

            return false;
        }
    }


    @Step("Get login error message text")
    public String getErrorMessage() {

        String errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        errorAlert
                )
        ).getText();

        takeScreenshot("Invalid Login Error Message");

        return errorMessage;
    }


    // =========================
    // FORGOT PASSWORD
    // =========================

    @Step("Click Forgot Password link")
    public void clickForgotPassword() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        forgotPasswordLink
                )
        ).click();

        takeScreenshot("Forgot Password Page");
    }


    @Step("Enter username for password reset: {username}")
    public void enterResetUsername(String username) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        resetUsernameInput
                )
        ).clear();

        driver.findElement(resetUsernameInput)
                .sendKeys(username);

        takeScreenshot("Enter Reset Username");
    }


    @Step("Submit password reset request")
    public void submitResetRequest() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        resetButton
                )
        ).click();

        takeScreenshot("Submit Password Reset Request");
    }


    // =========================
    // PASSWORD RESET VERIFICATION
    // =========================

    @Step("Check if reset success message is displayed")
    public boolean isResetSuccessDisplayed() {

        try {

            WebDriverWait resetWait =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(45)
                    );

            boolean displayed = resetWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            resetSuccessMessage
                    )
            ).isDisplayed();

            takeScreenshot("Password Reset Success Verification");

            return displayed;

        } catch (Exception e) {

            takeScreenshot(
                    "Password Reset Success Verification - Not Displayed"
            );

            return false;
        }
    }


    // =========================
    // 504 VERIFICATION
    // =========================

    @Step("Check if 504 Gateway Time-out is displayed")
    public boolean isGatewayTimeoutDisplayed() {

        try {

            WebDriverWait timeoutWait =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(45)
                    );

            boolean timeoutDisplayed = timeoutWait.until(
                    ExpectedConditions.or(
                            ExpectedConditions.visibilityOfElementLocated(
                                    gatewayTimeoutText
                            ),
                            ExpectedConditions.textToBePresentInElementLocated(
                                    By.tagName("body"),
                                    "504"
                            )
                    )
            );

            takeScreenshot("504 Gateway Timeout Verification");

            return timeoutDisplayed;

        } catch (Exception e) {

            takeScreenshot(
                    "504 Gateway Timeout Verification - Not Displayed"
            );

            return false;
        }
    }


    // =========================
    // ALLURE SCREENSHOT
    // =========================

    private void takeScreenshot(String name) {

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
                    "Unable to attach screenshot to Allure: "
                            + e.getMessage()
            );
        }
    }
}