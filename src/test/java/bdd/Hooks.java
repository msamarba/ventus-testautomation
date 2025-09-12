package bdd;

import io.cucumber.java.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.qameta.allure.Allure;

import java.time.Duration;

public class Hooks {
    public static ThreadLocal<WebDriver> DRIVER = new ThreadLocal<WebDriver>();

    public static WebDriver driver() { return DRIVER.get(); }

    @BeforeAll
    public static void setupClass() {
    }

    @Before
    public void openBrowser() {
        ChromeOptions opts = new ChromeOptions();
        // headless for CI; remove if you want to see the browser:
        opts.addArguments("--no-sandbox","--disable-dev-shm-usage");
        WebDriver d = new ChromeDriver(opts);
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        d.manage().window().setSize(new Dimension(1600, 1000));
        DRIVER.set(d);
    }

    @After
    public void quitBrowser(Scenario s) {
        try {
            if (s.isFailed()) {
                byte[] png = ((TakesScreenshot) driver()).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("failure", "image/png", "png", png);
            }
        } catch (Exception ignored) {}
        if (driver() != null) {
            driver().quit();
            DRIVER.remove();
        }
    }
}