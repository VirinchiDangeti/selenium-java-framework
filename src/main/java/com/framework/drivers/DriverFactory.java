package com.framework.drivers;

import com.framework.constants.BrowserType;
import com.framework.constants.FrameworkConstants;
import com.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Factory class responsible for creating WebDriver instances.
 *
 * Pattern: Factory Pattern
 *   → Caller just says "give me a driver"
 *   → Factory decides HOW to create it based on config
 *   → Caller doesn't care about browser-specific setup
 *
 * Supports: Chrome, Firefox, Edge
 * Reads browser type from: config.properties
 * Stores driver in: DriverManager (ThreadLocal)
 */
public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    // ─── Private constructor prevents instantiation ───
    private DriverFactory() {}

    /**
     * Main entry point — reads browser from config and initializes driver.
     * Called from BaseTest @BeforeMethod
     */
    public static void initDriver() {
        String browserName = ConfigReader.getOrDefault(
            "browser",
            FrameworkConstants.DEFAULT_BROWSER
        );
        BrowserType browser = BrowserType.fromString(browserName);
        initDriver(browser);
    }

    /**
     * Overloaded method — allows passing browser directly.
     * Useful for cross-browser testing via TestNG parameters.
     */
    public static void initDriver(BrowserType browser) {
        log.info("🌐 Initializing browser: " + browser);
        WebDriver driver;

        switch (browser) {
            case CHROME:
                driver = initChrome();
                break;
            case FIREFOX:
                driver = initFirefox();
                break;
            case EDGE:
                driver = initEdge();
                break;
            default:
                throw new IllegalArgumentException(
                    "❌ Unsupported browser: " + browser
                );
        }

        // ─── Apply common settings to all browsers ───
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(FrameworkConstants.IMPLICIT_WAIT));
        driver.manage().timeouts()
            .pageLoadTimeout(Duration.ofSeconds(FrameworkConstants.PAGE_LOAD_TIMEOUT));

        // ─── Store driver in ThreadLocal via DriverManager ───
        DriverManager.setDriver(driver);
        log.info("✅ Browser launched and configured successfully: " + browser);
    }

    // ═══════════════════════════════════════════════
    // PRIVATE METHODS — Browser-specific setup
    // ═══════════════════════════════════════════════

    /**
     * Initializes Chrome with custom options.
     */
    private static WebDriver initChrome() {
        // WebDriverManager auto-downloads correct chromedriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // ─── Read headless from config ───
        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new"); // new headless mode
            log.info("🔇 Chrome running in headless mode");
        }

        // ─── Disable automation flags ───
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
            new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // ─── Disable notifications & popups ───
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        // ─── Performance & stability ───
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");

        log.info("✅ ChromeOptions configured");
        return new ChromeDriver(options);
    }

    /**
     * Initializes Firefox with custom options.
     */
    private static WebDriver initFirefox() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless");
            log.info("🔇 Firefox running in headless mode");
        }

        options.addArguments("--disable-notifications");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        log.info("✅ FirefoxOptions configured");
        return new FirefoxDriver(options);
    }

    /**
     * Initializes Edge with custom options.
     */
    private static WebDriver initEdge() {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();

        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new");
            log.info("🔇 Edge running in headless mode");
        }

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--start-maximized");

        log.info("✅ EdgeOptions configured");
        return new EdgeDriver(options);
    }
}