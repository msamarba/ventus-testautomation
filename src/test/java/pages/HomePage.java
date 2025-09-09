package pages;

import org.openqa.selenium.*;

public class HomePage extends BasePage {
    public HomePage(WebDriver d){ super(d); }

    public HomePage open(String url){
        driver.navigate().to(url);
        return this;
    }

    public ContactPage goToKontakt(){
        // try header "Kontakt"; if a cookie banner exists, close it
        try {
            driver.findElement(By.cssSelector("button[aria-label*='Akzept' i],button:contains('Akzept')")).click();
        } catch (Exception ignored){}

        driver.findElement(By.linkText("Kontakt")).click();
        return new ContactPage(driver);
    }
}