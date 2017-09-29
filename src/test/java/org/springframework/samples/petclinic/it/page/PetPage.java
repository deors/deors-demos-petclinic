package org.springframework.samples.petclinic.it.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vicente.gonzalez
 *
 */
public class PetPage {

    private static final Logger logger = LoggerFactory.getLogger(PetPage.class);

    private WebDriver driver;

    private String baseUrl;

    // page title
    private String titleLabel = "Pet";

    // elements
    private By titleElement = By.xpath("//h2");
    private By mainContent = By.id("main");
    private By name = By.id("name");
    private By birthDate = By.id("birthDate");
    private By addPetCommand = By.id("addpet");
    private By deletePetCommand = By.id("deletepet");
    private By linkToHome = By.linkText("Home");

    /**
     * Constructor using a driver and a baseURL
     *
     * @param driver
     * @param baseUrl
     */
    public PetPage(WebDriver driver, String baseUrl) {

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
     * @return the label used for the title in the page
     */
    public String getTitleLabel() {

        return titleLabel;
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
     * Method that creates a Pet with the name and the birthdate received as parameters
     *
     * @param currentWebDriver
     * @param petName The name of the new Pet
     * @param petBirthDate The birthdate of the Pet
     */
    public void createPet(String petName, String petBirthDate) {

        logger.info("\t\t-- adding new pet");

        logger.debug("\t-- -- before create pet current URL is:" + driver.getCurrentUrl());

        driver.findElement(name).clear();
        driver.findElement(name).sendKeys(petName);
        driver.findElement(birthDate).clear();
        driver.findElement(birthDate).sendKeys(petBirthDate);
        driver.findElement(addPetCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9")
                && !d.getCurrentUrl().contains("pets/new"));

        logger.debug("\t-- -- after create pet current driver is:" + driver.getCurrentUrl());
    }

    /**
     *
     */
    public void deletePet() {

        logger.info("\t\t-- removing the pet");

        logger.debug("\t-- -- before deleting pet : current URL is:" + driver.getCurrentUrl());

        driver.findElement(deletePetCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().equals(baseUrl + "/owners/9"));

        logger.debug("\t-- -- after deleting pet : current driver is:" + driver.getCurrentUrl());
    }
}
