package org.springframework.samples.petclinic.it.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.samples.petclinic.it.SeleniumIntegrationTestBase.TIMEOUT;

public class HomePage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private WebDriver driver;

    private String baseUrl;

    // elements
    private By findOwnersCommand = By.linkText("Find owner");
    private By displayVeterinariansCommand = By.linkText("Display all veterinarians");
    private By displayTutorialCommand = By.linkText("Tutorial");
    private By linkToHome = By.linkText("Home");

    WebElement findOwnerLink;

    public HomePage(WebDriver driver, final String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    public void load() {
        logger.info("\t-- loading home page: " + baseUrl);

        // wait for the application to get fully loaded
        // wildfly returns 404 page when not loaded
        // so we have to navigate to home page until loaded
        // by looking at a specific element in the body
        // increase timeout to allow for app to be fully loaded
        WebElement findOwnerLink = (new WebDriverWait(driver, TIMEOUT * 10)).until(
            d -> {
                d.get(baseUrl);
                return d.findElement(By.linkText("Find owner"));
            });
    }

    public String getPageTitle() {

        String pageTitle = driver.getTitle();
        return pageTitle;
    }

    public String getUrl() {

        String pageURL = driver.getCurrentUrl();
        return pageURL;
    }

    /**
     * @return the link to the Find Owners page
     * @see WebElement
     */
    private WebElement getFindOwnerLink() {

        WebElement findOwnerLink = driver.findElement(findOwnersCommand);
        return findOwnerLink;
    }

    /**
     * Navigate to the FindOwnersPage
     *
     * @return the abstract representation of the FindOwnersPage
     * @see FindOwnersPage
     */
    public FindOwnersPage navigateToFindOwners() {

        logger.info("\t-- moving to find owners page");

        logger.debug("\t-- -- before moving to find owners page current URL is:" + driver.getCurrentUrl());

        // click on the link to the find owners page
        getFindOwnerLink().click();

        (new WebDriverWait(driver, TIMEOUT)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/search"));

        logger.debug("\t-- -- after moving to find owners page current URL is:" + driver.getCurrentUrl());

        return new FindOwnersPage(driver, baseUrl);
    }
}
