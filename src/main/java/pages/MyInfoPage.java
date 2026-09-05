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

public class MyInfoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public MyInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    // =========================
    // LOCATORS
    // =========================

    private final By myInfoMenuItem =
            By.xpath("//span[text()='My Info']");

    private final By emergencyContactsTab =
            By.xpath("//a[contains(text(),'Emergency Contacts')]");

    private final By addButton =
            By.xpath("//button[contains(.,'Add')]");

    private final By nameInput =
            By.xpath("//label[text()='Name']/following::input[1]");

    private final By relationshipInput =
            By.xpath("//label[text()='Relationship']/following::input[1]");

    private final By homeTelephoneInput =
            By.xpath("//label[text()='Home Telephone']/following::input[1]");

    private final By mobileInput =
            By.xpath("//label[text()='Mobile']/following::input[1]");

    private final By workTelephoneInput =
            By.xpath("//label[text()='Work Telephone']/following::input[1]");

    private final By saveButton =
            By.xpath("//button[@type='submit']");

    private final By successToast =
            By.xpath("//p[text()='Successfully Saved']");


    // =========================
    // BUG-002 LOCATORS
    // =========================

    private final By homeTelephoneLabel =
            By.xpath("//label[text()='Home Telephone']");

    private final By mobileLabel =
            By.xpath("//label[text()='Mobile']");

    private final By workTelephoneLabel =
            By.xpath("//label[text()='Work Telephone']");

    private final By phoneRequiredError =
            By.xpath(
                    "//span[contains(@class,'oxd-input-field-error-message') " +
                            "and contains(normalize-space(.)," +
                            "'At least one phone number is required')]"
            );

    private final By mobileFormatError =
            By.xpath(
                    "//label[text()='Mobile']/" +
                            "following::span[" +
                            "contains(@class,'oxd-input-field-error-message')" +
                            "][1]"
            );


    // =========================
    // NAVIGATION
    // =========================

    @Step("Navigate to My Info > Emergency Contacts")
    public void navigateToEmergencyContacts() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        myInfoMenuItem
                )
        ).click();

        takeScreenshot("My Info Page");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        emergencyContactsTab
                )
        ).click();

        takeScreenshot("Emergency Contacts Page");
    }


    // =========================
    // ADD CONTACT
    // =========================

    @Step("Click Add emergency contact button")
    public void clickAddEmergencyContact() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        addButton
                )
        ).click();

        takeScreenshot("Add Emergency Contact Form");
    }


    // =========================
    // CONTACT DATA
    // =========================

    @Step("Enter emergency contact name: {name}")
    public void enterName(String name) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        nameInput
                )
        ).clear();

        driver.findElement(nameInput)
                .sendKeys(name);

        takeScreenshot("Enter Emergency Contact Name");
    }


    @Step("Enter relationship: {relationship}")
    public void enterRelationship(String relationship) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        relationshipInput
                )
        ).clear();

        driver.findElement(relationshipInput)
                .sendKeys(relationship);

        takeScreenshot("Enter Relationship");
    }


    @Step("Enter home telephone number: {phone}")
    public void enterHomeTelephone(String phone) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        homeTelephoneInput
                )
        ).clear();

        driver.findElement(homeTelephoneInput)
                .sendKeys(phone);

        takeScreenshot("Enter Home Telephone");
    }


    @Step("Enter mobile number: {mobile}")
    public void enterMobile(String mobile) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        mobileInput
                )
        ).clear();

        driver.findElement(mobileInput)
                .sendKeys(mobile);

        takeScreenshot("Enter Mobile Number");
    }


    @Step("Enter work telephone number: {phone}")
    public void enterWorkTelephone(String phone) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        workTelephoneInput
                )
        ).clear();

        driver.findElement(workTelephoneInput)
                .sendKeys(phone);

        takeScreenshot("Enter Work Telephone");
    }


    // =========================
    // SAVE
    // =========================

    @Step("Click Save button")
    public void clickSave() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        saveButton
                )
        ).click();

        takeScreenshot("Click Save Button");
    }


    // =========================
    // SUCCESS VERIFICATION
    // =========================

    @Step("Check if success toast is displayed")
    public boolean isSuccessToastDisplayed() {

        try {

            boolean displayed = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            successToast
                    )
            ).isDisplayed();

            takeScreenshot("Success Toast Verification");

            return displayed;

        } catch (Exception e) {

            takeScreenshot(
                    "Success Toast Verification - Not Displayed"
            );

            return false;
        }
    }


    // =========================
    // BUG-002
    // =========================

    @Step("Check if any phone field label shows a mandatory (*) indicator")
    public boolean isAnyPhoneLabelMarkedRequired() {

        boolean home =
                driver.findElement(homeTelephoneLabel)
                        .getText()
                        .contains("*");

        boolean mobile =
                driver.findElement(mobileLabel)
                        .getText()
                        .contains("*");

        boolean work =
                driver.findElement(workTelephoneLabel)
                        .getText()
                        .contains("*");

        boolean required =
                home || mobile || work;

        takeScreenshot(
                required
                        ? "Phone Fields - Mandatory Indicator Found"
                        : "Phone Fields - No Mandatory Indicator"
        );

        return required;
    }


    @Step("Check if 'At least one phone number is required' error is displayed")
    public boolean isPhoneRequiredErrorDisplayed() {

        try {

            WebDriverWait errorWait =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(15)
                    );

            boolean displayed = errorWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            phoneRequiredError
                    )
            ).isDisplayed();

            takeScreenshot(
                    "Phone Required Validation Error"
            );

            return displayed;

        } catch (Exception e) {

            takeScreenshot(
                    "Phone Required Validation Error - Not Displayed"
            );

            return false;
        }
    }


    // =========================
    // MOBILE FORMAT VALIDATION
    // =========================

    @Step("Check if a format validation error is displayed near the Mobile field")
    public boolean isMobileFormatErrorDisplayed() {

        try {

            WebDriverWait shortWait =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(5)
                    );

            boolean displayed = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            mobileFormatError
                    )
            ).isDisplayed();

            takeScreenshot(
                    "Mobile Format Validation Error"
            );

            return displayed;

        } catch (Exception e) {

            takeScreenshot(
                    "Mobile Format Validation Error - Not Displayed"
            );

            return false;
        }
    }


    @Step("Get the format validation error text near the Mobile field")
    public String getMobileFormatErrorText() {

        String errorText =
                driver.findElement(
                        mobileFormatError
                ).getText();

        takeScreenshot(
                "Mobile Format Validation Error Text"
        );

        return errorText;
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
                            .getScreenshotAs(
                                    OutputType.BYTES
                            );

            Allure.addAttachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(
                            screenshot
                    ),
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