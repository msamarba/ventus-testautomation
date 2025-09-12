// Hooks.java
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Hooks {
    public static WebDriver driver;
    private Path tempProfileDir;

    @Before
    public void setUp() throws Exception {
        ChromeOptions opts = new ChromeOptions();
        // stability flags for CI:
        opts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
                "--no-first-run", "--no-default-browser-check",
                "--disable-extensions", "--disable-background-networking",
                "--remote-debugging-port=0");

        // allow override from CI, otherwise create a unique temp dir
        String ud = System.getProperty("chrome.userDataDir");
        if (ud == null || ud.isBlank()) {
            tempProfileDir = Files.createTempDirectory("chrome-profile-" + UUID.randomUUID());
            ud = tempProfileDir.toAbsolutePath().toString();
        }
        opts.addArguments("--user-data-dir=" + ud);

        driver = new ChromeDriver(opts);
    }

    @After
    public void tearDown() {
        try { if (driver != null) driver.quit(); } catch (Exception ignored) {}
        // optional: cleanup temp profile
        if (tempProfileDir != null) {
            try { org.openqa.selenium.io.FileHandler.delete(tempProfileDir.toFile()); } catch (Exception ignored) {}
        }
    }
}
