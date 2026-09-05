package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    @Description("Login with valid Admin credentials should redirect the user to the Dashboard page.")
    @Severity(SeverityLevel.BLOCKER)
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        Assert.assertTrue(loginPage.isDashboardDisplayed(),
                "Dashboard was not displayed after valid login.");
    }

    @Test(priority = 2, description = "Verify login fails with invalid credentials")
    @Description("Login with an invalid username/password combination should display an 'Invalid credentials' error and keep the user on the login page.")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("InvalidUser", "wrongPassword123");

        String actualError = loginPage.getErrorMessage();

        Assert.assertEquals(actualError, "Invalid credentials",
                "Error message did not match expected text for invalid login.");
        Assert.assertFalse(loginPage.isDashboardDisplayed(),
                "Dashboard should NOT be displayed after invalid login.");
    }

    @Test(priority = 3, description = "Verify Forgot Password flow with a valid username")
    @Description("BUG-001: Requesting a password reset for a valid Admin username is expected to display "
            + "a success message ('Reset Password link sent successfully'). Actual system behavior confirmed "
            + "via manual and automated verification: the request navigates to /auth/requestResetPassword and "
            + "returns a raw nginx '504 Gateway Time-out' error page after more than 30 seconds, indicating a "
            + "backend/reverse-proxy failure in the password reset email service, completely outside the "
            + "normal application UI.")
    @Severity(SeverityLevel.CRITICAL)
    public void testForgotPasswordWithValidUser() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.clickForgotPassword();
        loginPage.enterResetUsername("Admin");
        loginPage.submitResetRequest();

        boolean isTimeout = loginPage.isGatewayTimeoutDisplayed();
        boolean isSuccess = loginPage.isResetSuccessDisplayed();

        Assert.assertFalse(isTimeout,
                "BUG-001 confirmed: System returned a raw 504 Gateway Time-out page instead of the expected success message.");
        Assert.assertTrue(isSuccess,
                "Expected reset success message was not displayed - consistent with documented BUG-001 behavior.");
    }
}