package ua.deti.tqs.projetoapi.htmlpage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ServicesPrices extends HTMLPage {

    public ServicesPrices(WebDriver driver, int port) {
        super(driver, String.format("http://localhost:%d/service_info", port));

    }

    public void startNow() {
        try {
            WebElement submeter = driver.findElement(By.className("btn-primary"));
            submeter.click();
        } catch (Exception e) {
            LOGGER.error(ERROR + e.getMessage());
        }
    }

}
