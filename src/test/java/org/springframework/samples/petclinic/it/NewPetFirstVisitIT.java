package org.springframework.samples.petclinic.it;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class NewPetFirstVisitIT extends TestSetupITBase {

    private static final Logger logger = LoggerFactory.getLogger(NewPetFirstVisitIT.class);

    /**
    *
    */
   public NewPetFirstVisitIT() {

       super();
   }

    @Test
    public void testHtmlUnit()
        throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_HTMLUNIT);

        WebDriver driver = new HtmlUnitDriver();

        try {
            testNewPetFirstVisit(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testIE()
        throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_IE);

        Capabilities browser = DesiredCapabilities.internetExplorer();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);

        try {
            testNewPetFirstVisit(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testFirefox()
        throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_FIREFOX);

        Capabilities browser = DesiredCapabilities.firefox();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);

        try {
            testNewPetFirstVisit(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testChrome()
        throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_CHROME);

        Capabilities browser = DesiredCapabilities.chrome();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);

        try {
            testNewPetFirstVisit(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testOpera()
        throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_OPERA);

        Capabilities browser = DesiredCapabilities.operaBlink();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);

        try {
            testNewPetFirstVisit(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    public void testNewPetFirstVisit(final WebDriver driver, final String baseUrl) {

        driver.get(baseUrl);

        // wait for the application to get fully loaded
        WebElement findOwnerLink = (new WebDriverWait(driver, 5)).until(
            (Function<WebDriver, WebElement>) d -> d.findElement(By.linkText("Find owner")));

        findOwnerLink.click();

        (new WebDriverWait(driver, 5)).until((Predicate<WebDriver>) d
            -> d.getCurrentUrl().startsWith(baseUrl + "/owners/search"));

        driver.findElement(By.id("lastName")).clear();
        driver.findElement(By.id("lastName")).sendKeys("Schroeder");
        driver.findElement(By.id("findowners")).click();

        (new WebDriverWait(driver, 5)).until(
            (Predicate<WebDriver>) d -> d.getCurrentUrl().equals(baseUrl + "/owners/9"));

        assertTrue(driver.findElement(By.id("main")).getText().contains("David Schroeder"));
        driver.findElement(By.linkText("Add New Pet")).click();

        (new WebDriverWait(driver, 5)).until(
            (Predicate<WebDriver>) d -> d.getCurrentUrl().equals(baseUrl + "/owners/9/pets/new"));

        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Mimi");
        driver.findElement(By.id("birthDate")).clear();
        driver.findElement(By.id("birthDate")).sendKeys("2011-10-02");
        driver.findElement(By.id("addpet")).click();

        (new WebDriverWait(driver, 5)).until(
            (Predicate<WebDriver>) d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9")
                && !d.getCurrentUrl().contains("pets/new"));

        assertTrue(driver.findElement(By.id("main")).getText().contains("David Schroeder"));
        assertTrue(driver.findElement(By.id("main")).getText().contains("Mimi"));
        assertTrue(driver.findElement(By.id("main")).getText().contains("2011-10-02"));
        driver.findElement(By.linkText("Add Visit")).click();

        (new WebDriverWait(driver, 5)).until(
            (Predicate<WebDriver>) d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9/pets")
                && d.getCurrentUrl().contains("visits/new"));

        driver.findElement(By.id("date")).clear();
        driver.findElement(By.id("date")).sendKeys("2012-03-15");
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("rabies shot");
        driver.findElement(By.id("addvisit")).click();

        (new WebDriverWait(driver, 5)).until(
            (Predicate<WebDriver>) d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9")
                && !d.getCurrentUrl().contains("visits/new"));

        assertTrue(driver.findElement(By.id("main")).getText().contains("David Schroeder"));
        assertTrue(driver.findElement(By.id("main")).getText().contains("Mimi"));
        assertTrue(driver.findElement(By.id("main")).getText().contains("2011-10-02"));
        assertTrue(driver.findElement(By.id("main")).getText().contains("rabies shot"));
        driver.findElement(By.linkText("Edit Pet")).click();

        (new WebDriverWait(driver, 5)).until(
            (Predicate<WebDriver>) d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9/pets")
                && d.getCurrentUrl().contains("edit"));

        driver.findElement(By.id("deletepet")).click();

        (new WebDriverWait(driver, 5)).until(
            (Predicate<WebDriver>) d -> d.getCurrentUrl().equals(baseUrl + "/owners/9"));
    }

    @Test
    public void testAllElementsChrome()
        throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_CHROME);

        Capabilities browser = DesiredCapabilities.chrome();
        WebDriver driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);

        try {
            driver.get(TARGET_SERVER_URL);

            // wait for the application to get fully loaded
            WebElement findOwnerLink = (new WebDriverWait(driver, 5)).until(
                (Function<WebDriver, WebElement>) d -> d.findElement(By.linkText("Find owner")));

            logger.info("looking for elements in root of DOM");

            List<WebElement> elements = driver.findElements(By.xpath("//*"));

            logElements(elements);
            findElementAtCoordinates(elements, 100, 100).getTagName();
            findElementAtCoordinates(elements, 240, 100).getTagName();

            logger.info("looking for elements under top div element");

            elements = driver.findElements(By.xpath("//div/*"));

            logElements(elements);
            findElementAtCoordinates(elements, 100, 100);
            findElementAtCoordinates(elements, 240, 100);

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void logElements(List<WebElement> elements) {

        for (WebElement e: elements) {

            Point loc = e.getLocation();
            Dimension size = e.getSize();

            logger.info("  found element {} with content \"{}\" at position ({},{}) with size ({},{})",
                new Object[] {e.getTagName(), e.getText(), loc.getX(), loc.getY(), size.getWidth(), size.getHeight()});
        }
    }

    private WebElement findElementAtCoordinates(List<WebElement> elements, int x, int y) {

        for (WebElement e: elements) {

            Point pos = e.getLocation();
            Dimension size = e.getSize();

            if (x >= pos.getX()
                && x <= pos.getX() + size.getWidth()
                && y >= pos.getY()
                && y <= pos.getY() + size.getHeight()) {

                logger.info("  found element {} at position ({},{})",
                    new Object[] {e.getTagName(), x, y});

                return e;
            }
        }

        logger.info("  no element found at position ({},{})",
            new Object[] {x, y});

        return null;
    }
}
