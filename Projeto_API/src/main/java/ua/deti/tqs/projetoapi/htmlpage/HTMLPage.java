package ua.deti.tqs.projetoapi.htmlpage;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class HTMLPage {

    protected static final String ERROR = "ERROR! :";
    protected static final Logger LOGGER = Logger.getLogger(HTMLPage.class);
    protected WebDriver driver;

    public HTMLPage(WebDriver driver, String url) {
        this.driver = driver;

        String baseUrl = url;
        driver.get(baseUrl + "/");
    }
}
