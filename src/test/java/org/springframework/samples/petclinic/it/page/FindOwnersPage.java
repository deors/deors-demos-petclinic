package org.springframework.samples.petclinic.it.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindOwnersPage {

    private static final Logger logger = LoggerFactory.getLogger(FindOwnersPage.class);

    private WebDriver driver;

    private String baseUrl;

    // label of the title of this page
    private String titleLabel = "Owner Information";

    // elements
    private By titleElement = By.xpath("//h2");
    private By lastNameField = By.id("lastName");
    private By addOwnerCommand = By.id("addOwner");
    private By findOwnersCommand = By.id("findowners");

    /**
     * Constructor for the FindOwnersPage object with the driver and baseURL as parameters
     *
     * @param driver
     * @param baseUrl
     */
    public FindOwnersPage(WebDriver driver, String baseUrl) {

        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    /**
     * @return the HTML title
     */
    public String getPageTitle() {

        String pageTitle = driver.getTitle();
        return pageTitle;
    }

    /**
     * @return the URL of the current page.
     */
    public String getUrl() {

        String pageURL = driver.getCurrentUrl();
        return pageURL;
    }

    /**
     * @return title label of the page (window title is the same for all pages)
     */
    public String getTitleLabel() {

        return titleLabel;
    }

    /**
     * Method used to verify that the page loaded is the one expected. This is done by matching the
     * Label of the title within the header tags
     *
     * @return true if the page contains the title label expected
     */
    public boolean verifyPage() {

        WebElement title = driver.findElement(titleElement);
        logger.debug("\t-- -- verifying page with title: \"" + title.getText() + "\" contains(\"" + getTitleLabel()+"\")");
        return title.getText().contains(getTitleLabel());
    }

    /**
     * This method invokes the search of the Owner passed by the parameter ownerToFind. It first
     * clear the content of the LastName input and then populated the input with the value of the
     * ownerToFind parameter
     *
     * @param ownerToFind the owner to look for.
     * @return the {@link org.springframework.samples.petclinic.it.page.OwnerPage} that represents
     *         the Owner Page where data of the owner are maintained
     */
    public OwnerPage findOwner(String ownerToFind) {

        logger.info("\t-- getting owner information" );

        logger.debug("\t-- -- before find owner "+ ownerToFind + "current URL:"+ driver.getCurrentUrl());

        driver.findElement(lastNameField).clear();
        driver.findElement(lastNameField).sendKeys(ownerToFind);
        driver.findElement(findOwnersCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().equals(baseUrl + "/owners/9"));

        logger.debug("\t-- -- after find owner: current URL:" + driver.getCurrentUrl());

        return new OwnerPage(driver, baseUrl);
    }
}
