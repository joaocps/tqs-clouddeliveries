package ua.deti.tqs.projetoapi.htmlpage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Index extends HTMLPage {

    public Index(WebDriver driver, int port) {
        super(driver, String.format("http://localhost:%d", port));

    }

   
    public void submit() {
        try {
            WebElement submeter = driver.findElement(By.id("homepage"));
            submeter.click();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
        }
    }

    public String getTitle() {
        try {
            WebElement element = driver.findElement(By.id("title"));
            return element.getText();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
            return "no";
        }
    }

    public String getSubtitle() {
        try {
            WebElement element = driver.findElement(By.id("subtitle"));
            return element.getText();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
            return "no";
        }
    }

    
}
