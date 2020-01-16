/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.java.changeInfo;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.junit.Assert.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;

import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ChangeInfoFeatureTest {

    static WebDriver driver;

    @LocalServerPort
    private int port;

    @Autowired
    private UserRep userRep;

    @Autowired
    private OrderRep orderRep;

    @Autowired
    private ProductCategoryRep catRep;

    private User user;

    private ProductCategory cat;

    private Order order;

    private boolean order_shows;
    
    private String address = "Aveiro";

    static {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);

    }

    public void login() {
        driver.get(String.format("http://localhost:%d/login", port));
        driver.findElement(By.id("email")).sendKeys(user.getEmail());
        driver.findElement(By.id("password")).sendKeys(user.getPassword());
        driver.findElement(By.name("login-submit")).submit();
    }

    @Given("^Pedro is logged in$")
    public void loggedin() {
        userRep.deleteAll();
        catRep.deleteAll();
        user = new User("pedro@test.pt", "test");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setAddress(address);
        user.setPhoneNumber(999999999);
        user.setCountry("Portugal-Continental");
        userRep.save(user);

        cat = new ProductCategory("category");
        catRep.save(cat);
        login();
    }

    @Given("^he is in the changeuserprofile page$")
    public void userHomePage() {
        driver.get(String.format("http://localhost:%d/changeuserprofile", port));
    }

   
    @When("^he inserts a correct \"([^\"]*)\"$")
    public void correct_credentials(String field) {
    	switch (field){
    	case "address":
    		driver.findElement(By.id("address")).clear();
            driver.findElement(By.id("address")).sendKeys("Tomar");
            break;
    	case "firstaname":
    		driver.findElement(By.id("firstName")).clear();
            driver.findElement(By.id("firstName")).sendKeys("Lucas");
            break;
    	}
    	
        
    }
    
    @When("^he inserts incorrect fields$")
    public void incorrect_credentials() {
    	driver.findElement(By.id("phoneNumber")).clear();
        driver.findElement(By.id("phoneNumber")).sendKeys(address);
        
    }
    
    @When("^then clicks 'submit'$")
    public void clicks_submit() {
        driver.findElement(By.id("sub")).submit();
        
    }
    
   
    
    @Then("^is redirected to userhome$")
    public void to_user_home(){
        assertTrue(driver.getCurrentUrl().contains("userhome"));
    }
    
    @Then("^is redirected to error page$")
    public void to_error_home(){
        assertTrue(driver.getCurrentUrl().contains("error"));
    }
    
    @Then("^his \"([^\"]*)\" is updated$")
    public void info_updated(String field){
    	switch(field){
    	case "address":
    		assertEquals("Tomar", userRep.findById(user.getId()).get().getAddress());
    		break;
    	case "firstname":
    		assertEquals("Lucas", userRep.findById(user.getId()).get().getFirstName());
    		break;
    	}
    	
        
    }

    @Then("^his fields are not updated$")
    public void info_not_updated(){
        System.out.println(userRep.findAll());
        assertTrue(userRep.findById(user.getId()).get().getAddress().equals(address));
    }
}
