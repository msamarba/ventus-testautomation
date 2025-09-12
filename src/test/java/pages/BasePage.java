package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement byText(String text){
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[normalize-space()='" + text + "']")));
    }

    protected WebElement inputNearLabel(String label){
        return driver.findElement(By.xpath(
                "//label[contains(.,'" + label + "')]/../div/input | " +
                        "//label[contains(.,'" + label + "')]/following::textarea"
        ));
    }
}