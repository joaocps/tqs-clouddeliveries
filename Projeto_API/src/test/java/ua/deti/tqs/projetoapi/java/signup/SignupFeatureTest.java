/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.java.signup;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.Assert.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.entities.UserType;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.UserTypeRep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class SignupFeatureTest {

	static WebDriver driver;


	@LocalServerPort
	private int port;

	@Autowired
	UserRep rep;
	
	@Autowired
	UserTypeRep typeRep;
	
	User user;
	
	private long num_accounts;

	static {
		ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
	}



	@Given("^Pedro is in signup page$")
	public void getToSigninPage(){
		typeRep.deleteAll();
		UserType type = new UserType(10, 100);
		UserType type2 = new UserType(100, 1000);
		UserType type3 = new UserType(1000, 10000);
		typeRep.save(type);
		typeRep.save(type2);
		typeRep.save(type3);
		driver.get(String.format("http://localhost:%d/sign_up", port));
	}
	
	@Given("^his account is not created$")
	public void clean_account(){
		rep.deleteAll();
		
		num_accounts = rep.count();
	}



	@When("^he inserts correct credentials$")
	public void goodCredentials(){
		
		user = new User("TESTFEATURE", "TESTFEATURE", "TESTFEATURE");
		user.setAddress("TEST");
		user.setPhoneNumber(999999999);
		user.setEmail("TEST@ua.pt");
		user.setCountry("Portugal-Continental");
		driver.findElement(By.id("first_name")).sendKeys(user.getFirstName());
		driver.findElement(By.id("last_name")).sendKeys(user.getLastName());
		driver.findElement(By.id("password")).sendKeys(user.getPassword());
		driver.findElement(By.id("address")).sendKeys(user.getAddress());
		driver.findElement(By.id("number")).sendKeys(Integer.toString(user.getPhoneNumber()));
		driver.findElement(By.id("email")).sendKeys(user.getEmail());
		Select select = new Select(driver.findElement(By.id("country")));
		select.selectByVisibleText(user.getCountry());
	}


	@When("^he inserts incorrect credentials$")
	public void badCredentials(){
		
		User user = new User("TESTFEATURE", "TESTFEATURE", "TESTFEATURE");
		user.setAddress("TEST");
		user.setEmail("TEST@ua.pt");
		user.setCountry("Portugal-Continental");
		driver.findElement(By.id("first_name")).sendKeys(user.getFirstName());
		driver.findElement(By.id("last_name")).sendKeys(user.getLastName());
		driver.findElement(By.id("password")).sendKeys(user.getPassword());
		driver.findElement(By.id("address")).sendKeys(user.getAddress());
		driver.findElement(By.id("number")).sendKeys("01skdj");
		driver.findElement(By.id("email")).sendKeys(user.getLastName());
		Select select = new Select(driver.findElement(By.id("country")));
		select.selectByVisibleText(user.getCountry());    

	}

	@Then("^he chooses his user type$")
	public void UserType(){
		driver.findElement(By.className("btn-primary")).click();
	}
	
	@When("^then clicks ‘Register’$")
	public void clickLogin(){
		driver.findElement(By.id("submeter")).submit();
	}
	
	


	@Then("^a CloudDeliveries account is created$")
	public void created(){
		assertTrue(rep.count() == 1);
	}
	
	@Then("^a CloudDeliveries account is not created$")
	public void notCreated(){
		assertTrue(rep.count() == 0);
	}

	
	@Then("^he is redirected to 'choose service' page$")
	public void toServicePage(){
		assertTrue(driver.getCurrentUrl().contains("choosetype"));
	}

	@Then("^he is redirected to 'login page'$")
	public void toLoginPage(){
		assertTrue(driver.getCurrentUrl().contains("login"));
	}
	
	
	
	@Then("^he is redirected to 'error' page$")
	public void toErrorPage(){
		assertTrue(driver.getCurrentUrl().contains("error"));
	}

	

}
