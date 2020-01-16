/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.java.makerequest;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.entities.UserType;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.repositories.UserTypeRep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;


import ua.deti.tqs.projetoapi.ProjetoApiApplication;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MakerequestFeatureTest {

	static WebDriver driver;


	@LocalServerPort
	private int port;
	
	@Autowired
	OrderRep orderRep;
	
	@Autowired
	UserRep userRep;
	
	@Autowired
	UserTypeRep typeRep;
	
	@Autowired
	ProductCategoryRep catRep;
	
	User user;
	
	Order order;
	
	ProductCategory cat;

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
	
	public void setup(){
		userRep.deleteAll();
		orderRep.deleteAll();
		catRep.deleteAll();
		typeRep.deleteAll();
		ProductCategory cat = new ProductCategory("category");
		catRep.save(cat);
		UserType type = new UserType(1, 10);
		typeRep.save(type);
		user = new User("pedro@test.pt", "test", type);
		order = new Order("name", cat, user);

	}

	@Given("^Pedro is logged-in$")
	public void isLoggedIn(){
		setup();
        userRep.save(user);
		login();
	}
	
	@Given("^Pedro is on the make-order page$")
	public void makeRequestPage(){
		driver.get(String.format("http://localhost:%d/makeRequest", port));
		
	}
	
	
	
	@Given("^cant make a request$")
	public void cantRequest(){
		user.setOrders(Arrays.asList(order));
	}
	
	@Given("^can make a request$")
	public void canRequest(){
		user.setOrders(new ArrayList<>());
	}
	
	@When("^he enters correct credentials$")
	public void goodCredentials(){
		driver.findElement(By.id("name")).sendKeys("A Piano");
		driver.findElement(By.id("address")).sendKeys("Tomar, Rua de Coimbra, nº20");
		driver.findElement(By.id("phone")).sendKeys("918201928");
		Select select = new Select(driver.findElement(By.id("typeproduct")));
		select.selectByVisibleText("category");
	}
	
	@When("^he enters incorrect credentials$")
	public void badCredentials(){
		driver.findElement(By.id("name")).sendKeys("A Piano");
		driver.findElement(By.id("address")).sendKeys("Tomar, Rua de Coimbra, nº20");
		driver.findElement(By.id("phone")).sendKeys("918nskdfb");
		Select select = new Select(driver.findElement(By.id("typeproduct")));
		select.selectByVisibleText("category");
	}
	
	
	@When("^then clicks ‘submit’$")
	public void submit() throws InterruptedException{
		driver.findElement(By.id("submit")).click();
		
	}

	
	@Then("^he gets redirected to the error page$")
	public void toErrorPage(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
		assertTrue(driver.getCurrentUrl().contains("error"));
	}
	
	@Then("^he gets redirected to the userhome page$")
	public void toUserHomePage(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("num_requests")));
		assertTrue(driver.getCurrentUrl().contains("userhome"));
	}
	
	@Then("^the order is not created$")
	public void orderNotCreated() throws InterruptedException{
		assertEquals(0, orderRep.count());
	}
	
	@Then("^the order is created$")
	public void orderCreated(){
		assertEquals(1 , orderRep.count());
	}


	


	
	

}
