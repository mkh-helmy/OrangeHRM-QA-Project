package com.orangehrm.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DirectoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public DirectoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private final By directoryMenuItem = By.xpath("//span[text()='Directory']");
    private final By employeeNameInput = By.xpath("//label[text()='Employee Name']/following::input[1]");
    private final By invalidFieldMessage = By.xpath("//span[text()='Invalid']");

    @Step("Navigate to Directory module")
    public void navigateToDirectory() {
        wait.until(ExpectedConditions.elementToBeClickable(directoryMenuItem)).click();
    }

    @Step("Type an employee name that does not exist in the system: {name}")
    public void typeNonExistentEmployeeName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameInput)).sendKeys(name);
        driver.findElement(employeeNameInput).sendKeys(Keys.TAB);
    }

    @Step("Check if 'Invalid' validation message is displayed for unmatched employee name")
    public boolean isInvalidMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(invalidFieldMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}