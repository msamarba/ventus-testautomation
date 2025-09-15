package bdd;

import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.*;
import java.time.Duration;
import java.util.UUID;

public class Hooks {
    public static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<Path> PROFILE_DIR = new ThreadLocal<>();

    public static WebDriver driver() { return DRIVER.get(); }

    @Before
    public void openBrowser() throws Exception {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments(
                "--headless=new","--no-sandbox","--disable-dev-shm-usage",
                "--no-first-run","--no-default-browser-check",
                "--disable-extensions","--disable-background-networking",
                "--remote-debugging-port=0"
        );

        // ONLY add a user-data-dir if provided, else let Chrome create an ephemeral profile
        String base = System.getProperty("chrome.userDataDir");
        if (base != null && !base.trim().isEmpty()) {
            Path dir = Paths.get(base, "t" + Thread.currentThread().getId() + "-" + UUID.randomUUID());
            Files.createDirectories(dir);
            PROFILE_DIR.set(dir);
            opts.addArguments("--user-data-dir=" + dir.toAbsolutePath());
        }

        WebDriver d = new ChromeDriver(opts);
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        d.manage().window().setSize(new Dimension(1600, 1000));
        DRIVER.set(d);
    }

    @After
    public void quitBrowser(Scenario s) {
        try {
            WebDriver d = driver();
            if (s.isFailed() && d instanceof TakesScreenshot) {
                byte[] png = ((TakesScreenshot) d).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("failure","image/png","png",png);
            }
        } catch (Exception ignored) {}

        try { if (driver()!=null) driver().quit(); } catch (Exception ignored) {}
        DRIVER.remove();

        // clean temp profile if we created one
        Path dir = PROFILE_DIR.get();
        PROFILE_DIR.remove();
        if (dir != null) {
            try {
                Files.walk(dir).sorted((a,b)->b.compareTo(a)).forEach(p -> { try { Files.deleteIfExists(p); } catch(Exception ignored){} });
            } catch (Exception ignored) {}
        }
    }
}