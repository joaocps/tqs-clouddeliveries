package ua.deti.tqs.projetoapi.htmlpage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Home extends HTMLPage {

    public Home(WebDriver driver, int port) {
        super(driver, String.format("http://localhost:%d/home", port));

    }

    public void servicesAndPrices() {
        try {
            WebElement submeter = driver.findElement(By.id("services"));
            submeter.click();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
        }
    }

    public void about() {
        try {
            WebElement submeter = driver.findElement(By.id("about"));
            submeter.click();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
        }
    }

    public void signup() {
        try {
            WebElement submeter = driver.findElement(By.id("signup"));
            submeter.click();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
        }
    }

    public void login() {
        try {
            WebElement submeter = driver.findElement(By.id("login"));
            submeter.click();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
        }
    }
    
    public int getNumberComments(){
    	try {
    		return driver.findElements(By.className("comment")).size();
    		
    	} catch(Exception e){
    		LOGGER.error(ERROR + e.getMessage());
    		return 0;
    	}
    }
}
