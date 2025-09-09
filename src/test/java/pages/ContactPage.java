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

    public ContactPage submit(){
        try {
            driver.findElement(By.xpath("//button[.//text()[contains(.,'Senden')] or contains(.,'Senden')]")).click();
        } catch (NoSuchElementException e) {
            driver.findElement(By.tagName("button")).click();
        }
        return this;
    }

    public boolean confirmationVisible(){
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(.,'Vielen Dank') or contains(.,'Danke')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(.,'erfolgreich')]"))
            ));
            return true;
        } catch (TimeoutException t){ return false; }
    }
}