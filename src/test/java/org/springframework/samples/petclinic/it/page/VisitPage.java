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
public class VisitPage {

    private static final Logger logger = LoggerFactory.getLogger(VisitPage.class);

    private WebDriver driver;

    private String baseUrl;

    // page title
    private String titleLabel = "Visit";

    // elements
    private By mainContent = By.id("main");
    private By titleElement = By.xpath("//h2");
    private By visitForm = By.id("visit");
    private By dateField = By.id("date");
    private By descriptionField = By.id("description");
    private By addVisitCommand = By.id("addvisit");
    private By linkToHome = By.linkText("Home");

    /**
     * Constructor using a driver and a baseURL
     *
     * @param driver
     * @param baseUrl
     */
    public VisitPage(WebDriver driver, String baseUrl) {

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
        logger.debug("-- -- page text: " + pageText);

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
     * Method that creates a visit for a given pet at the date received by parameters and with the
     * description provided
     *
     * @param date
     * @param description
     */
    public void createVisit(String date, String description) {

        logger.info("\t-- creating a visit for the pet: ");

        logger.debug("\t-- -- before create visit: current URL:" + driver.getCurrentUrl());

        driver.findElement(dateField).clear();
        driver.findElement(dateField).sendKeys(date);
        driver.findElement(descriptionField).clear();
        driver.findElement(descriptionField).sendKeys(description);
        driver.findElement(addVisitCommand).click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9")
                && !d.getCurrentUrl().contains("pets/new"));

        logger.debug("\t-- -- after create visit current driver is:" + driver.getCurrentUrl());
    }
}
