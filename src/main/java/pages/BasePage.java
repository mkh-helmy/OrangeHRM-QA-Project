package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private final By pageLoader = By.xpath("//div[contains(@class,'oxd-form-loader')] | //div[contains(@class,'oxd-loading-spinner')]");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected void click(By locator) {
        int attempts = 0;
        RuntimeException lastException = null;
        while (attempts < 3) {
            try {
                waitForLoaderToDisappear();
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                lastException = e;
                attempts++;
            }
        }
        throw lastException;
    }

    protected void jsClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void type(By locator, String text) {
        int attempts = 0;
        RuntimeException lastException = null;
        while (attempts < 3) {
            try {
                waitForLoaderToDisappear();
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                el.clear();
                el.sendKeys(text);
                return;
            } catch (StaleElementReferenceException e) {
                lastException = e;
                attempts++;
            }
        }
        throw lastException;
    }

    protected void waitForLoaderToDisappear() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOfElementLocated(pageLoader));
        } catch (Exception ignored) {
            // Loader wasn't present or already gone - fine either way
        }
    }

    protected boolean isVisible(By locator, int timeoutSeconds) {
        try {
            WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return localWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected By topTab(String label) {
        return By.xpath(String.format(
                "//*[self::a or self::span or self::li or self::button][normalize-space(.)='%s']", label));
    }

    protected By button(String label) {
        return By.xpath(String.format("//button[normalize-space(.)='%s']", label));
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }
}