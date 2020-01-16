/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.java.login;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang.RandomStringUtils;
import static org.junit.Assert.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class LoginFeatureTest {

    static WebDriver driver;

    @LocalServerPort
    private int port;

    @Autowired
    UserRep userService;
    
    User user;

    static {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);

    }

    public void setup() {
        userService.deleteAll();
        user = new User("test@ua.pt", "test");
        userService.saveAndFlush(user);
    }

    @Given("^Pedro is on the log-in page$")
    public void getToLoginPage() {
        setup();
        driver.get(String.format("http://localhost:%d/login", port));
    }

    @When("^he enters correct credentials$")
    public void goodCredentials() {
        
        driver.findElement(By.id("email")).sendKeys(user.getEmail());
        driver.findElement(By.name("password")).sendKeys(user.getPassword());
    }

    @When("^he enters incorrect credentials$")
    public void badCredentials() {
        System.out.println("aqui");
        driver.findElement(By.id("email")).sendKeys(RandomStringUtils.random(10, true, true));
        driver.findElement(By.name("password")).sendKeys(RandomStringUtils.random(10, true, true));

    }

    @When("^then clicks ‘log In’$")
    public void clickLogin() {
        driver.findElement(By.name("login-submit")).submit();
    }

    @Then("^he gets redirected to the Home page$")
    public void toHomePage() {
        assertTrue(driver.getCurrentUrl().contains("userhome"));
    }

    @Then("^he gets redirected to an error page$")
    public void toErrorPage() {
        assertTrue(driver.getCurrentUrl().contains("error"));
    }

    @Then("^user object session is created$")
    public void user_object_session_is_created() {
        driver.get(String.format("http://localhost:%d/makeRequest", port));
        assertTrue(driver.getCurrentUrl().contains("makeRequest"));
    }

}
