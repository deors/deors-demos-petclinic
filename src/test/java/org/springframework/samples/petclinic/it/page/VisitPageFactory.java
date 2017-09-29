package org.springframework.samples.petclinic.it.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vicente.gonzalez
 *
 */
public class VisitPageFactory {

    private static final Logger logger = LoggerFactory.getLogger(VisitPageFactory.class);

    private WebDriver driver;

    private String baseUrl;

    // page title
    private String titleLabel = "Visit";

    // elements

    @FindBy(id="main")
    WebElement mainContent;

    @FindBy(xpath="//h2")
    WebElement titleElement;

    @FindBy(id="visit")
    WebElement visitForm;

    @FindBy(id="date")
    WebElement dateField;

    @FindBy(id="description")
    WebElement descriptionField;

    @FindBy(id="addvisit")
    WebElement addVisitCommand;

    @FindBy(linkText="Home")
    WebElement linkToHome;

    /**
     * Constructor using a driver and a baseURL
     *
     * @param driver
     * @param baseUrl
     */
    public VisitPageFactory(WebDriver driver, String baseUrl) {

        logger.info("****Ey JavaOners... I am now using a Factory!!!!");
        this.driver = driver;
        this.baseUrl = baseUrl;
        PageFactory.initElements(driver, this);

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

        String pageText = mainContent.getText();
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

        logger.debug("\t-- -- verifying page with title: \"" + titleElement.getText() + "\" contains(\"" + getTitleLabel()+"\")");
        return titleElement.getText().contains(getTitleLabel());
    }

    /**
     * Method that creates a visit for a given pet at the date received by parameters and with the
     * description provided
     *
     * @param date
     * @param description
     */
    public void createVisit(String date, String description) {

        logger.info("\t-- creating a visit");

        logger.debug("\t-- -- before create visit current driver is:" + driver.getCurrentUrl());

        dateField.clear();
        dateField.sendKeys(date);
        descriptionField.clear();
        descriptionField.sendKeys(description);
        addVisitCommand.click();

        (new WebDriverWait(driver, 5)).until(
            d -> d.getCurrentUrl().startsWith(baseUrl + "/owners/9")
                && !d.getCurrentUrl().contains("pets/new"));

        logger.debug("\t-- -- after create visit current driver is:" + driver.getCurrentUrl());
    }
}
