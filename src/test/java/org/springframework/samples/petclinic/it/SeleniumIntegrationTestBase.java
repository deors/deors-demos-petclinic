package org.springframework.samples.petclinic.it;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumIntegrationTestBase {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumIntegrationTestBase.class);

    protected static boolean RUN_HTMLUNIT;

    protected static boolean RUN_IE;

    protected static boolean RUN_FIREFOX;

    protected static boolean RUN_CHROME;

    protected static boolean RUN_OPERA;

    protected static String SELENIUM_HUB_URL;

    protected static String TARGET_SERVER_URL;

    @BeforeClass
    public static void initEnvironment() {

        RUN_HTMLUNIT = getConfigurationProperty("RUN_HTMLUNIT", "test.run.htmlunit", true);

        logger.info("running the tests in HtmlUnit: " + RUN_HTMLUNIT);

        RUN_IE = getConfigurationProperty("RUN_IE", "test.run.ie", false);

        logger.info("running the tests in Internet Explorer: " + RUN_IE);

        RUN_FIREFOX = getConfigurationProperty("RUN_FIREFOX", "test.run.firefox", false);

        logger.info("running the tests in Firefox: " + RUN_FIREFOX);

        RUN_CHROME = getConfigurationProperty("RUN_CHROME", "test.run.chrome", false);

        logger.info("running the tests in Chrome: " + RUN_CHROME);

        RUN_OPERA = getConfigurationProperty("RUN_OPERA", "test.run.opera", false);

        logger.info("running the tests in Opera: " + RUN_OPERA);

        SELENIUM_HUB_URL = getConfigurationProperty(
            "SELENIUM_HUB_URL", "test.selenium.hub.url", "http://localhost:4444/wd/hub");

        logger.info("using Selenium hub at: " + SELENIUM_HUB_URL);

        TARGET_SERVER_URL = getConfigurationProperty(
            "TARGET_SERVER_URL", "test.target.server.url", "http://localhost:58080/petclinic");

        logger.info("using target server at: " + TARGET_SERVER_URL);
    }

    private static String getConfigurationProperty(String envKey, String sysKey, String defValue) {

        String retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = sysValue;
        } else if (envValue != null) {
            retValue = envValue;
        }
        return retValue;
    }

    private static boolean getConfigurationProperty(String envKey, String sysKey, boolean defValue) {

        boolean retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = Boolean.parseBoolean(sysValue);
        } else if (envValue != null) {
            retValue = Boolean.parseBoolean(envValue);
        }
        return retValue;
    }
}
