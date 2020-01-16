package ua.deti.tqs.projetoapi.interfaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ua.deti.tqs.projetoapi.entities.Comment;
import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.CommentRep;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.htmlpage.Home;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class HomeTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	CommentRep comRep;
	
	@Autowired
	UserRep userRep;
	
	@Autowired
	OrderRep orderRep;
	
	@Autowired
	ProductCategoryRep catRep;
	
	private WebDriver driver;
    private Home home;
    
    
    @Before
    public void setUp() throws Exception { 
    	ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        

        orderRep.deleteAll();
        userRep.deleteAll();
        catRep.deleteAll();
        
        ProductCategory cat = new ProductCategory("category");
        
        User user = new User("Lucas", "Barros", "pass");
        
        Order order1 = new Order("name",  cat, user);
        Order order2 = new Order("name2", cat, user);
    
        Comment com1 = new Comment(4, "muito bom!!");
        Comment com2 = new Comment(5, "muito bom, recomendo!!");
        com1.setOrder(order1);
        com2.setOrder(order2);
        
        order1.setComment(com1);
        order2.setComment(com2);
        
        
        catRep.save(cat);
        userRep.save(user);
        orderRep.saveAll(Arrays.asList(order1, order2));
        comRep.saveAll(Arrays.asList(com1, com2));
        home = new Home(this.driver, port);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("hhhhhh"))));
    }
    
    
    
    @Test
    public void servicesPricesTest(){
    	home.servicesAndPrices();
    	assertTrue(driver.getCurrentUrl().contains("service_info"));
    }
    
    @Test
    public void aboutTest(){
    	home.about();
    	assertTrue(driver.getCurrentUrl().contains("about"));
    }
    
    @Test
    public void signup(){
    	home.signup();
    	assertTrue(driver.getCurrentUrl().contains("sign_up"));
    }
    
    @Test
    public void login(){
    	home.login();
    	assertTrue(driver.getCurrentUrl().contains("login"));
    }
    
    
    
    @Test
    public void commentsShowsUp(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("comment"))));
    	int comments = home.getNumberComments();
    	assertEquals(2, comments);
    }
    
    @After
    public void tearDown() throws Exception {
        comRep.deleteAll();
        driver.close();

    }

}
