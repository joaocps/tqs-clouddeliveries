package ua.deti.tqs.projetoapi.htmlpage.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ua.deti.tqs.projetoapi.htmlpage.HTMLPage;

public class UserHome extends HTMLPage {

    public UserHome(WebDriver driver, int port) {
        super(driver, String.format("http://localhost:%d/userhome", port));

    }

    
    public int getNumberRequests() {
        try {
            return Integer.parseInt(driver.findElement(By.id("num_requests")).getText());
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
            return -1;
        }
    }

    public int getNumberRows() {
        try {
            return driver.findElements(By.xpath("//table[@id='tabela']/tbody/tr")).size();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
            return 0;
        }
    }

    public void profile() {

        WebElement submeter = driver.findElement(By.id("profile"));
        submeter.click();

    }

    public void request() {

        WebElement submeter = driver.findElement(By.id("makerequest"));
        submeter.click();

    }

    public void home() {

        WebElement submeter = driver.findElement(By.id("userhome"));
        submeter.click();

    }

}
