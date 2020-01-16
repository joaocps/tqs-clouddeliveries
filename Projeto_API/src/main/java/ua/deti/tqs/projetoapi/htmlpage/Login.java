/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.htmlpage;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Login {

	private static final String EMAIL = "email";
	private static final String PASS = "password";
    private static final String ERROR = "ERROR! :";
    private static final Logger LOGGER = Logger.getLogger(Login.class);
    private WebDriver driver;

    public Login(WebDriver driver) {
        this.driver = driver;

        String baseUrl = "http://localhost:8080/login";
        driver.get(baseUrl + "/");
    }

    public void submit() {
        try {
            WebElement submeter = driver.findElement(By.name("login-submit"));
            submeter.click();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
        }
    }

    public String getEmail() {
        try {
            return  driver.findElement(By.name(EMAIL)).getAttribute("value");

        } catch (Exception e) {
            return EMAIL;
        }

    }

    public String getPassword() {
        try {
            return driver.findElement(By.name(PASS)).getAttribute("value");
      
        } catch (Exception e) {
            return PASS;
        }
    }

    public void setEmail(String email) {
        WebElement element = driver.findElement(By.id(EMAIL));
        element.sendKeys(email);
    }

    public void setPassword(String password) {
        WebElement element = driver.findElement(By.id(PASS));
        element.sendKeys(password);
    }

}
