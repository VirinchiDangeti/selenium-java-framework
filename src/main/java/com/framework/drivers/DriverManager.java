package com.framework.drivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;


public final class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);

    // ─── ThreadLocal holds one WebDriver per thread ───
    private static final ThreadLocal<WebDriver> driverThreadLocal =
        new ThreadLocal<>();

    // ─── Private constructor prevents instantiation ───
    private DriverManager() {}

    /**
     * Returns the WebDriver for the CURRENT thread.
     * Every thread gets its OWN driver instance.
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Sets the WebDriver for the CURRENT thread.
     * Called by DriverFactory after creating a browser instance.
     */
    public static void setDriver(WebDriver driver) {
        log.info("🔧 Setting WebDriver for thread: "
            + Thread.currentThread().getName());
        driverThreadLocal.set(driver);
    }

    /**
     * Quits the WebDriver and removes it from ThreadLocal.
     *
     * WHY remove():
     *   Thread pools REUSE threads.
     *   If we don't remove → next test on same thread
     *   gets the OLD driver → causes failures!
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("🔴 Quitting WebDriver for thread: "
                + Thread.currentThread().getName());
            driver.quit();
            driverThreadLocal.remove(); // ← CRITICAL step
            log.info("✅ WebDriver removed from ThreadLocal");
        }
    }

    /**
     * Checks if driver is initialized for current thread.
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }
}
