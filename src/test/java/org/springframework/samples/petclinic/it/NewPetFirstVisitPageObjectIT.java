package org.springframework.samples.petclinic.it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assume;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.it.page.FindOwnersPage;
import org.springframework.samples.petclinic.it.page.HomePage;
import org.springframework.samples.petclinic.it.page.OwnerPage;
import org.springframework.samples.petclinic.it.page.PetPage;
import org.springframework.samples.petclinic.it.page.VisitPage;
import org.springframework.samples.petclinic.it.page.VisitPageFactory;

public class NewPetFirstVisitPageObjectIT
    extends SeleniumITBase {

    private static final Logger logger = LoggerFactory.getLogger(NewPetFirstVisitPageObjectIT.class);

    @Test
    public void testHtmlUnit() throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_HTMLUNIT);

        logger.info("executing test in htmlunit");

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
    public void testIE() throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_IE);

        logger.info("executing test in internet explorer");

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
    public void testFirefox() throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_FIREFOX);

        logger.info("executing test in firefox");

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
    public void testChrome() throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_CHROME);

        logger.info("executing test in chrome");

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
    public void testOpera() throws MalformedURLException, IOException {

        Assume.assumeTrue(RUN_OPERA);

        logger.info("executing test in opera");

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

        HomePage homePage = new HomePage(driver, baseUrl);

        FindOwnersPage findOwnersPage = homePage.navigateToFindOwners();

        OwnerPage ownerPage = findOwnersPage.findOwner("Schroeder");
        assertTrue(ownerPage.getName().contains("David Schroeder"));

        PetPage petPage = ownerPage.navigateToAddNewPet();

        petPage.createPet("Mimi", "2011-10-02");
        assertTrue(ownerPage.getName().contains("David Schroeder"));
        assertTrue(ownerPage.getMainText().contains("Mimi"));
        assertTrue(ownerPage.getMainText().contains("2011-10-02"));

        VisitPage visitPage = ownerPage.navigateToAddNewVisit();
        // alternative way using the PageFactory pattern
        //VisitPageFactory visitPage = ownerPage.navigateToAddNewVisitFromFactory();

        visitPage.createVisit("2012-03-15", "rabies shot");
        assertTrue(ownerPage.getName().contains("David Schroeder"));
        assertTrue(ownerPage.getMainText().contains("Mimi"));
        assertTrue(ownerPage.getMainText().contains("2012-03-15"));
        assertTrue(ownerPage.getMainText().contains("rabies shot"));

        petPage = ownerPage.navigateToEditPet();
        assertTrue("pet page not found", petPage.verifyPage());

        petPage.deletePet();
        assertTrue(ownerPage.getName().contains("David Schroeder"));
        assertFalse(ownerPage.getMainText().contains("Mimi"));
    }
}
