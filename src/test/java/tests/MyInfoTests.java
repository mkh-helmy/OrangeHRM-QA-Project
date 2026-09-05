package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.MyInfoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MyInfoTests extends BaseTest {

    @Test(priority = 1, description = "Verify mandatory phone number validation lacks a visual indicator")
    @Description("BUG-002 (TC-022): The system correctly enforces a mandatory phone number rule - saving is "
            + "blocked with 'At least one phone number is required' if all phone fields are left blank, and "
            + "format validation correctly rejects non-numeric input. This validation behavior itself is "
            + "correct and expected. However, none of the phone number fields (Home Telephone, Mobile, Work "
            + "Telephone) display a mandatory asterisk (*) to signal this requirement to the user before "
            + "submission, leaving them unaware of the rule until after a failed save attempt.")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingMandatoryIndicatorOnPhoneFields() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        MyInfoPage myInfoPage = new MyInfoPage(driver);
        myInfoPage.navigateToEmergencyContacts();
        myInfoPage.clickAddEmergencyContact();

        boolean anyPhoneFieldMarkedRequired = myInfoPage.isAnyPhoneLabelMarkedRequired();

        Assert.assertTrue(anyPhoneFieldMarkedRequired,
                "BUG-002 confirmed: None of the phone fields (Home Telephone, Mobile, Work Telephone) "
                        + "display a mandatory (*) indicator, even though the system enforces a mandatory "
                        + "phone number rule upon saving.");
    }

    @Test(priority = 2, description = "Verify invalid phone number format is rejected in the Mobile field")
    @Description("The Mobile field on the Emergency Contacts screen correctly validates input and rejects "
            + "non-numeric characters, displaying a clear error message ('Allows numbers and only + - / ( )') "
            + "to the user. This confirms the validation behavior works consistently across the Home "
            + "Telephone and Mobile fields.")
    @Severity(SeverityLevel.MINOR)
    public void testInvalidPhoneNumberFormat() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        MyInfoPage myInfoPage = new MyInfoPage(driver);
        myInfoPage.navigateToEmergencyContacts();
        myInfoPage.clickAddEmergencyContact();

        myInfoPage.enterName("Validation Test Contact");
        myInfoPage.enterRelationship("Sister");
        myInfoPage.enterMobile("abcdefg");
        myInfoPage.clickSave();

        boolean isErrorDisplayed = myInfoPage.isMobileFormatErrorDisplayed();

        Assert.assertTrue(isErrorDisplayed,
                "Expected a format validation error near the Mobile field for non-numeric input ('abcdefg').");

        if (isErrorDisplayed) {
            String errorText = myInfoPage.getMobileFormatErrorText();
            Assert.assertTrue(errorText.toLowerCase().contains("number") || errorText.contains("+"),
                    "Displayed error message does not match the expected format-validation pattern: " + errorText);
        }
    }
}