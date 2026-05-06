package com.framework.constants;

public final class FrameworkConstants {
	private FrameworkConstants() {
        throw new UnsupportedOperationException(
            "FrameworkConstants is a utility class and cannot be instantiated!"
        );
    }

    // ─── Config File Path ───
    public static final String CONFIG_FILE_PATH =
        "src/test/resources/config/config.properties";

    // ─── Timeout Constants (seconds) ───
    public static final int IMPLICIT_WAIT    = 10;
    public static final int EXPLICIT_WAIT    = 15;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    public static final int FLUENT_WAIT      = 20;
    public static final int POLLING_TIME     = 2;

    // ─── Report Constants ───
    public static final String REPORT_PATH  =
        System.getProperty("user.dir") + "/reports/TestExecutionReport.html";
    public static final String REPORT_TITLE = "Selenium BDD Automation Report";
    public static final String REPORT_NAME  = "Test Execution Report";

    // ─── Screenshot Constants ───
    public static final String SCREENSHOT_PATH =
        System.getProperty("user.dir") + "/screenshots/";

    // ─── Log Constants ───
    public static final String LOG_PATH =
        System.getProperty("user.dir") + "/logs/framework.log";

    // ─── Test Data Constants ───
    public static final String EXCEL_DATA_PATH =
        "src/test/resources/testdata/TestData.xlsx";
    public static final String JSON_DATA_PATH =
        "src/test/resources/testdata/TestData.json";

    // ─── Default Browser ───
    public static final String DEFAULT_BROWSER = "chrome";

    // ─── Default URL ───
    public static final String DEFAULT_URL = "https://www.saucedemo.com";

}
