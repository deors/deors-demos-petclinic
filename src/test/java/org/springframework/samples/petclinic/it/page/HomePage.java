package org.springframework.samples.petclinic.it.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.info("\t-- home page: " + driver.getCurrentUrl());
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

        WebElement findOwnerLink =
            (new WebDriverWait(driver, 5)).until(
                d -> d.findElement(findOwnersCommand));
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

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/search"));

        logger.debug("\t-- -- afger moving to find owners page current URL is:" + driver.getCurrentUrl());

        return new FindOwnersPage(driver, baseUrl);
    }
}
