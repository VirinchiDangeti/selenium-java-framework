package com.framework.base;

import com.framework.config.ConfigReader;
import com.framework.constants.BrowserType;
import com.framework.drivers.DriverFactory;
import com.framework.drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

/**
 * BaseTest — Parent class for ALL test classes.
 *
 * WHY:
 *   Every test needs a WebDriver.
 *   Every test needs to open a URL before testing.
 *   Every test needs to quit the driver after.
 *
 *   Instead of repeating this in every test class →
 *   put it here ONCE and all tests inherit it!
 *
 * Pattern: Template Method Pattern
 *   → BaseTest defines the skeleton (setup/teardown)
 *   → Child test classes fill in the actual test steps
 *
 * How to use:
 *   public class LoginTest extends BaseTest { ... }
 */
public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    /**
     * @BeforeMethod → runs before EACH @Test method
     *
     * Reads browser from testng.xml parameter if provided,
     * otherwise falls back to config.properties value.
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        log.info("═══════════════════════════════════════");
        log.info("🚀 Setting up test environment...");

        // ─── Use browser from testng.xml, else from config ───
        if (browser != null && !browser.isEmpty()) {
            log.info("🌐 Browser from testng.xml: " + browser);
            DriverFactory.initDriver(BrowserType.fromString(browser));
        } else {
            log.info("🌐 Browser from config.properties");
            DriverFactory.initDriver();
        }

        // ─── Navigate to application URL ───
        String url = ConfigReader.get("url");
        DriverManager.getDriver().get(url);
        log.info("🔗 Navigated to URL: " + url);
    }

    /**
     * @AfterMethod → runs after EACH @Test method
     * Quits the driver and cleans up ThreadLocal
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("🔴 Tearing down test environment...");
        DriverManager.quitDriver();
        log.info("✅ Driver quit successfully");
        log.info("═══════════════════════════════════════");
    }

    /**
     * Helper method to get WebDriver from anywhere in test class.
     * Child classes call this to interact with browser.
     *
     * Usage in test: getDriver().findElement(...)
     */
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}