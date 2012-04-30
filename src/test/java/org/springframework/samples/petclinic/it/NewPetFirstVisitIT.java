package org.springframework.samples.petclinic.it;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewPetFirstVisitIT {

    private static final Logger logger = LoggerFactory.getLogger(NewPetFirstVisitIT.class);

    private static String SELENIUM_HUB_URL;

    private static String SELENIUM_ANDROID_HUB_URL;

    private static String TARGET_SERVER_URL;

    @BeforeClass
    public static void initEnvironment() {

        SELENIUM_HUB_URL = getConfigurationProperty(
            "SELENIUM_HUB_URL",
            "test.selenium.hub.url",
            "http://localhost:4444/wd/hub");

        logger.info("using Selenium hub at: " + SELENIUM_HUB_URL);

        SELENIUM_ANDROID_HUB_URL = getConfigurationProperty(
            "ANDROID_HUB_URL",
            "test.selenium.android.hub.url",
            "http://localhost:4448/wd/hub");

        logger.info("using Selenium Android hub at: " + SELENIUM_ANDROID_HUB_URL);

        TARGET_SERVER_URL = getConfigurationProperty(
            "TARGET_SERVER_URL",
            "test.target.server.url",
            "http://localhost:8180/petclinic");

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

    @Test
    public void testIE()
        throws MalformedURLException, IOException {

        DesiredCapabilities browser = DesiredCapabilities.internetExplorer();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
        testNewPetFirstVisit(driver);
    }

    @Test
    public void testFirefox()
        throws MalformedURLException, IOException {

        DesiredCapabilities browser = DesiredCapabilities.firefox();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
        testNewPetFirstVisit(driver);
    }

    @Test
    public void testChrome()
        throws MalformedURLException, IOException {

        DesiredCapabilities browser = DesiredCapabilities.chrome();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
        testNewPetFirstVisit(driver);
    }

//    @Test
//    public void testOpera()
//        throws MalformedURLException, IOException {
//
//        DesiredCapabilities browser = DesiredCapabilities.opera();
//        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
//        testNewPetFirstVisit(driver);
//    }

//    @Test
//    public void testAndroid()
//        throws MalformedURLException, IOException {
//
//        WebDriver driver = new AndroidDriver(new URL(SELENIUM_ANDROID_HUB_URL));
//        testNewPetFirstVisit(driver);
//    }

    public void testNewPetFirstVisit(WebDriver driver) {

        try {
            driver.get(TARGET_SERVER_URL);
            driver.findElement(By.linkText("Find owner")).click();

            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().startsWith(TARGET_SERVER_URL + "/owners/search");
                }
            });

            driver.findElement(By.id("lastName")).clear();
            driver.findElement(By.id("lastName")).sendKeys("S");
            driver.findElement(By.id("lastName")).sendKeys("chroeder");
            driver.findElement(By.id("findowners")).click();

            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().startsWith(TARGET_SERVER_URL + "/owners/9");
                }
            });

            assertTrue(driver.findElement(By.id("main")).getText().contains("David Schroeder"));
            driver.findElement(By.linkText("Add New Pet")).click();

            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().startsWith(TARGET_SERVER_URL + "/owners/9/pets/new");
                }
            });

            driver.findElement(By.id("name")).clear();
            driver.findElement(By.id("name")).sendKeys("M");
            driver.findElement(By.id("name")).sendKeys("imi");
            driver.findElement(By.id("birthDate")).clear();
            driver.findElement(By.id("birthDate")).sendKeys("2011-10-02");
            driver.findElement(By.id("addpet")).click();

            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().startsWith(TARGET_SERVER_URL + "/owners/9")
                        && !d.getCurrentUrl().contains("pets/new");
                }
            });

            assertTrue(driver.findElement(By.id("main")).getText().contains("David Schroeder"));
            assertTrue(driver.findElement(By.id("main")).getText().contains("Mimi"));
            assertTrue(driver.findElement(By.id("main")).getText().contains("2011-10-02"));
            driver.findElement(By.linkText("Add Visit")).click();

            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().startsWith(TARGET_SERVER_URL + "/owners/9/pets")
                        && d.getCurrentUrl().contains("visits/new");
                }
            });

            driver.findElement(By.id("date")).clear();
            driver.findElement(By.id("date")).sendKeys("2012-03-15");
            driver.findElement(By.id("description")).clear();
            driver.findElement(By.id("description")).sendKeys("rabies shot");
            driver.findElement(By.id("addvisit")).click();

            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().startsWith(TARGET_SERVER_URL + "/owners/9")
                        && !d.getCurrentUrl().contains("visits/new");
                }
            });

            assertTrue(driver.findElement(By.id("main")).getText().contains("David Schroeder"));
            assertTrue(driver.findElement(By.id("main")).getText().contains("Mimi"));
            assertTrue(driver.findElement(By.id("main")).getText().contains("2011-10-02"));
            assertTrue(driver.findElement(By.id("main")).getText().contains("rabies shot"));
            driver.findElement(By.linkText("Edit Pet")).click();

            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().startsWith(TARGET_SERVER_URL + "/owners/9/pets")
                        && d.getCurrentUrl().contains("edit");
                }
            });

            driver.findElement(By.id("deletepet")).click();

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
