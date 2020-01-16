/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.java.logout;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.UserRep;

import static org.junit.Assert.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class LogoutFeatureTest {

	static WebDriver driver;
	
	@Autowired
	UserRep rep;

	@LocalServerPort
	private int port;

	User user;
	
	static {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        
        

	}
	
	public void login(){
        driver.get(String.format("http://localhost:%d/login", port));
        driver.findElement(By.id("email")).sendKeys(user.getEmail());
        driver.findElement(By.id("password")).sendKeys(user.getPassword());
        driver.findElement(By.name("login-submit")).submit();
    }

	
	@Given("^Pedro is logged in$")
	public void loggedin(){
		rep.deleteAll();
		
        user = new User("pedro@test.pt", "test");
        rep.save(user);
		login();
	}


	
	@Given("^is on the \"([^\"]*)\" page$")
	public void getPage(String page){
		driver.get(String.format("http://localhost:%d/%s", port, page));
	}
	
	
	@When("^he trys to logout$")
	public void goodCredentials(){
		driver.findElement(By.id("logout")).click();
	}



	@Then("^he gets redirected to the home page$")
	public void toHomePage(){
		assertTrue(driver.getCurrentUrl().contains("home"));
	}


	@Then("^the session is cleaned$")
	public void toErrorPage(){
		driver.get(String.format("http://localhost:%d/userhome", port));
		assertTrue(driver.getCurrentUrl().contains("error"));
	}

	

}
