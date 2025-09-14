package bdd;

import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

public class Hooks {
    public static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<Path> PROFILE_DIR = new ThreadLocal<>();

    public static WebDriver driver() { return DRIVER.get(); }

    @Before
    public void openBrowser() throws Exception {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments(
                "--no-sandbox","--disable-dev-shm-usage",
                "--no-first-run","--no-default-browser-check",
                "--disable-extensions","--disable-background-networking",
                "--remote-debugging-port=0"
        );

        // base can be injected from CI; otherwise create a temp profile per thread
        String base = System.getProperty("chrome.userDataDir", "");
        Path dir = base.isBlank()
                ? Files.createTempDirectory("chrome-profile-" + UUID.randomUUID() + "-" + Thread.currentThread().getId())
                : Paths.get(base, "t" + Thread.currentThread().getId() + "-" + UUID.randomUUID());
        Files.createDirectories(dir);
        PROFILE_DIR.set(dir);
        opts.addArguments("--user-data-dir=" + dir.toAbsolutePath());

        WebDriver d = new ChromeDriver(opts);
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        d.manage().window().setSize(new Dimension(1600, 1000));
        DRIVER.set(d);
    }

    @After
    public void quitBrowser(Scenario s) {
        try {
            if (s.isFailed() && driver() instanceof TakesScreenshot ts) {
                Allure.getLifecycle().addAttachment("failure","image/png","png", ts.getScreenshotAs(OutputType.BYTES));
            }
        } catch (Exception ignored) {}

        try { if (driver()!=null) driver().quit(); } catch (Exception ignored) {}
        DRIVER.remove();

        // best-effort cleanup of the profile dir
        Path dir = PROFILE_DIR.get();
        PROFILE_DIR.remove();
        if (dir != null) {
            try (Stream<Path> walk = Files.walk(dir)) {
                walk.sorted((a,b) -> b.compareTo(a)).forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
            } catch (IOException ignored) {}
        }
    }
}
