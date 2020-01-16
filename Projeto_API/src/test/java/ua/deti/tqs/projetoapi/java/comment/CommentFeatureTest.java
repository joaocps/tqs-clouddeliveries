/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.java.comment;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.Assert.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.State;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentFeatureTest {

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
		userRep.deleteAll();
		catRep.deleteAll();
        user = new User("pedro@test.pt", "test");
        userRep.save(user);
        
        cat = new ProductCategory("category");
        catRep.save(cat);
		login();
	}
	
	@Given("^is on userhome page$")
	public void userHomePage(){
		driver.get(String.format("http://localhost:%d/userhome", port));
	}
	
	@Given("^no order is finished$")
	public void no_order_finished(){
		orderRep.deleteAll();
		order = new Order("order test", cat, user);
		orderRep.save(order);
	}
	
	@Given("^at least one order is finished$")
	public void one_order(){
		orderRep.deleteAll();
		order = new Order("order test", cat, user);
		order.setState(State.FINISH);
		orderRep.save(order);
	}
	
	private boolean isDisplayed(String id) throws InterruptedException {
        try {
           WebDriverWait wait = new WebDriverWait(driver, 5);
           wait.until(
        		   ExpectedConditions.visibilityOfElementLocated(By.id(id)));
           return driver.findElement(By.id(id)).isDisplayed();
        }
        catch (Exception ex) {
          return false;
        }
    }
	
	@When("^Pedro searches for the button to comment$")
	public void searchForButton() throws InterruptedException{
		order_shows = isDisplayed(Integer.toString(order.getId()));
	}
	
	
	
	@When("^Pedro comments$")
	public void comments() throws InterruptedException{
		
		
	}
	
	@Then("^the button shows$")
	public void button_shows(){
		assertTrue(order_shows);
	}
	
	@Then("^nothing shows up$")
	public void nothing_shows(){
		assertFalse(order_shows);
	}
	
	
	@Then("^the user writes and introduces the comment$")
	public void introduces_comment() throws InterruptedException{
		driver.findElement(By.id(Integer.toString(order.getId()))).click();
		boolean shows_up = isDisplayed("exampleModal");
		assertTrue(shows_up);
		WebElement ele = driver.findElement(By.id("comment"));
		String js = "arguments[0].setAttribute('value','My comment')";
				((JavascriptExecutor) driver).executeScript(js, ele);
		//driver.findElement(By.id("submit")).click();
		
	}
	
	
	
	
	
	
	
	/*

	@Then("^the comment is saved$")
	public void comment_is_saved() throws SQLException{
		Connection con = getcon();
		ResultSet set;
		PreparedStatement ps = null;
		ps = con.prepareStatement("SELECT comment_id from orders WHERE orders.id=" + order.getId());
		set = ps.executeQuery();
		assertTrue(set.next());
	}
	
	public Connection getcon(){
	    try{
	    	System.out.println("get con");
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String unicode="useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8";
	        return DriverManager.getConnection("jdbc:mysql://192.168.160.6:9002/fulltests_db?" + unicode, "root", "qwerty");
	    }catch(Exception ex){
	        System.out.println(ex.getMessage());
	        System.out.println("couldn't connect!");
	        throw new RuntimeException(ex);
	    }
	}
	
	*/

}
