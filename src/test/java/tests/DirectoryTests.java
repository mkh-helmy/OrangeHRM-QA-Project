package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DirectoryPage;
import com.orangehrm.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DirectoryTests extends BaseTest {

    @Test(priority = 1, description = "Verify Directory rejects a name with no matching employee")
    @Description("The Directory module's Employee Name field is a type-ahead search that requires selecting "
            + "from suggested matches. Typing a name with no matching employee is expected to display an "
            + "'Invalid' validation message rather than allowing an open-ended search, preventing the user "
            + "from submitting a query that cannot resolve to a real employee.")
    @Severity(SeverityLevel.MINOR)
    public void testSearchWithNonExistentEmployeeName() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        DirectoryPage directoryPage = new DirectoryPage(driver);
        directoryPage.navigateToDirectory();
        directoryPage.typeNonExistentEmployeeName("Zzxxqqvvnonexistent");

        Assert.assertTrue(directoryPage.isInvalidMessageDisplayed(),
                "'Invalid' validation message was not displayed for a non-matching employee name.");
    }
}