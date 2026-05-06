package com.framework.constants;

public enum  BrowserType {
	 	CHROME,
	    FIREFOX,
	    EDGE;

	    /**
	     * Converts a String to BrowserType enum safely.
	     * Used when reading browser name from config.properties
	     *
	     * Example:
	     *   BrowserType.fromString("chrome") → BrowserType.CHROME
	     *   BrowserType.fromString("FIREFOX") → BrowserType.FIREFOX
	     */
	    public static BrowserType fromString(String browser) {
	        switch (browser.trim().toUpperCase()) {
	            case "CHROME":  return CHROME;
	            case "FIREFOX": return FIREFOX;
	            case "EDGE":    return EDGE;
	            default:
	                throw new IllegalArgumentException(
	                    "❌ Browser not supported: " + browser +
	                    " | Supported: chrome, firefox, edge"
	                );
	        }
	    }
	}