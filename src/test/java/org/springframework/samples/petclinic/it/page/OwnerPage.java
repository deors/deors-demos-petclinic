package org.springframework.samples.petclinic.it.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstraction for OwnerPage
 *
 * @author vicente.gonzalez
 *
 */
public class OwnerPage {

    private static final Logger logger = LoggerFactory.getLogger(OwnerPage.class);

    private WebDriver driver;

    private String baseUrl;

    // page title
    private String titleLabel = "Owner Information";

    // Elements
    private By titleElement = By.xpath("//h2");
    private By mainContent = By.id("main");
    private By editOwnerCommand = By.linkText("Edit Owner");
    private By addNewPetCommand = By.linkText("Add New Pet");
    private By editPetCommand = By.linkText("Edit Pet");
    private By addVisitCommand = By.linkText("Add Visit");
    private By linkToHome = By.linkText("Home");
    private By nameTag = By.xpath("//table[1]/tbody/tr[1]/td");
    private By addressTag = By.xpath("//table[1]/tbody/tr[2]/td");


    /**
     * OwnerPage abstraction constructor
     *
     * @param driver
     * @param baseUrl
     */
    public OwnerPage(WebDriver driver, String baseUrl) {

        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    /**
     * @return title page
     */
    public String getPageTitle() {

        String pageTitle = driver.getTitle();
        return pageTitle;
    }

    /**
     * @return current URL
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
     * @return the full text within the "main" id
     */
    public String getMainText() {

        WebElement element = driver.findElement(mainContent);
        String pageText = element.getText();
        logger.debug("\t-- -- page text: " + pageText);

        return pageText;
    }

    public String getName() {

        WebElement element = driver.findElement(nameTag);
        String name = element.getText();
        logger.debug("\t-- -- name:" + element.getText());
        return name;
    }

    public String getAddress() {

        WebElement element = driver.findElement(addressTag);
        String address = element.getText();
        logger.debug("\t-- -- adress:" + element.getText());
        return address;
    }

    /**
     * Navigate to the PetPage for adding pets to a selected owner
     *
     * @return the abstraction for the New Pet Page
     *
     * @see PetPage
     */
    public PetPage navigateToAddNewPet() {

        logger.info("\t-- moving to the new pet page for adding a pet");

        logger.debug("\t-- -- before navigate to add new pet: current URL is:"+  driver.getCurrentUrl());

        driver.findElement(addNewPetCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().equals(baseUrl + "/owners/9/pets/new"));

        logger.debug("\t-- -- after navigate to add new pet: current driver is:" + driver.getCurrentUrl());

        return new PetPage(driver, baseUrl);
    }

    /**
     * Navigate to the PetPage for adding pets to a selected owner
     *
     * @return the abstraction for the New Pet Page
     *
     * @see PetPage
     */
    public PetPage navigateToEditPet() {

        logger.info("\t-- moving to the edit pet page for removing the pet");

        logger.debug("\t-- -- before navigate to edit pet: current URL is:" + driver.getCurrentUrl());

        driver.findElement(editPetCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9/pets")
                && d.getCurrentUrl().contains("edit"));

        logger.debug("\t-- -- after navigate to edit pet: current URL is:" + driver.getCurrentUrl());

        return new PetPage(driver, baseUrl);
    }

    /**
     * Navigate to the AddVisit page for the selected owner
     * @return the abstraction for the New Visit Page
     *
     * @see VisitPage
     */
    public VisitPage navigateToAddNewVisit() {

        logger.info("\t-- moving to the new visit page for adding a visit for the pet ");

        logger.debug("-- -- before navigate to add new visit: current URL is:" + driver.getCurrentUrl());

        driver.findElement(addVisitCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9/pets")
            && d.getCurrentUrl().contains("visits/new"));

        logger.debug("\t-- -- after navigate to add new visit: current driver is:" + driver.getCurrentUrl());

        return new VisitPage(driver, baseUrl);
    }

    /**
     * Navigate to the AddVisit page for the selected owner
     * @return the abstraction for the New Visit Page
     *
     * @see VisitPageFactory
     */
    public VisitPageFactory navigateToAddNewVisitFromFactory() {

        logger.info("\t-- moving to the new visit page for adding a visit for the pet ");

        logger.debug("\t-- -- before navigate to add new visit: current URL is:" + driver.getCurrentUrl());

        driver.findElement(addVisitCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9/pets")
                && d.getCurrentUrl().contains("visits/new"));

        logger.debug("\t-- -- after navigate to add new visit: current URL is:" + driver.getCurrentUrl());

        return new VisitPageFactory(driver, baseUrl);
    }
}
