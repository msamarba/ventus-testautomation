package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ContactPage extends BasePage {
    public ContactPage(WebDriver d){ super(d); }

    public ContactPage fill(String field, String value){
        if (value == null) return this;
        inputNearLabel(field).clear();
        inputNearLabel(field).sendKeys(value);
        return this;
    }

    public ContactPage acceptPrivacy(){
        // look for Datenschutz/Einwilligung type checkbox
        try {
            WebElement cb = driver.findElement(By.cssSelector("input[type='checkbox']"));
            if (!cb.isSelected()) cb.click();
        } catch (Exception ignored){}
        return this;
    }

    public void submit(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@aria-label='Senden']")));
            driver.findElement(By.xpath("//button[@aria-label='Senden']")).click();
    }

    public boolean confirmationVisible(){
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(.,'Verifizierung')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(.,'erfolgreich')]"))
            ));
            return true;
        } catch (TimeoutException t){ return false; }
    }
}