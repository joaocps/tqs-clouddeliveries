package ua.deti.tqs.projetoapi.interfac.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import io.github.bonigarcia.wdm.WebDriverManager;
import ua.deti.tqs.projetoapi.entities.Order;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.htmlpage.user.UserHome;
import org.springframework.test.context.TestPropertySource;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.State;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserHomeTest {

    @LocalServerPort
    private int port;
 
    private static WebDriver driver;
    private UserHome userhome;
    
    ChromeOptions options;
    private WebDriverWait wait;

    @Autowired
    OrderRep rep;

    @Autowired
    UserRep userRep;
    
    @Autowired
    ProductCategoryRep categoryRep;
    
    User user;
    
    Order order;
    
    Order order2;
    
    

    @Before
    public void setUp() throws Exception {
        userRep.deleteAll();
        rep.deleteAll();
        categoryRep.deleteAll();
        
        user = new User("test@ua.pt", "test");
        userRep.save(user);
        ProductCategory cat = new ProductCategory("category");
        categoryRep.save(cat);
        order = new Order("test1", cat, user);
        order2 = new Order("test2", cat, user);
        order2.setState(State.FINISH);
        rep.save(order);
        rep.save(order2);
        

        options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        userhome = new UserHome(driver, port);

    }
    
    public void login(){
        driver.get(String.format("http://localhost:%d/login", port));
        driver.findElement(By.id("email")).sendKeys(user.getEmail());
        driver.findElement(By.id("password")).sendKeys(user.getPassword());
        driver.findElement(By.name("login-submit")).submit();
    }

    
    public void waiit() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("profile")));
    }
    

    @Test
    public void profileTest() {
    	login();
    	driver.get(String.format("http://localhost:%d/userhome", port));
        driver.findElement(By.id("profile")).click();
        assertTrue(driver.getCurrentUrl().contains("userprofile"));
    }
    
    

    @Test
    public void requestTest() {
    	login();
    	driver.get(String.format("http://localhost:%d/userhome", port));
        userhome.request();
        assertTrue(driver.getCurrentUrl().contains("makeRequest"));
    }

    @Test
    public void homeTest() {
    	login();
    	driver.get(String.format("http://localhost:%d/userhome", port));
        userhome.home();
        assertTrue(driver.getCurrentUrl().contains("userhome"));
    }


    @Test
    public void request2_numRows() {
    	login();
    	driver.get(String.format("http://localhost:%d/userhome", port));
        System.out.println(userhome.getNumberRows());
        assertEquals(2, userhome.getNumberRequests());
        assertEquals(2, userhome.getNumberRows());
    }
    
    

    @Test
    public void comment_button() {
    	login();
    	driver.get(String.format("http://localhost:%d/userhome", port));
    	assertTrue(driver.findElement(By.id(Integer.toString(order2.getId()))).isDisplayed());
    	
    }
    
    @Test
    public void modal_open() throws InterruptedException{
    	login();
    	driver.get(String.format("http://localhost:%d/userhome", port));
    	driver.findElement(By.id(Integer.toString(order2.getId()))).click();
    	assertTrue(isDisplayed());
    }
    
    
    private Boolean isDisplayed() throws InterruptedException {
        try {
           WebDriverWait wait = new WebDriverWait(driver, 5);
           wait.until(
        		   ExpectedConditions.visibilityOfElementLocated(By.id("exampleModal")));
           return driver.findElement(By.id("exampleModal")).isDisplayed();
        }
        catch (Exception ex) {
          return false;
        }
    }
	

    @After
    public void tearDown() throws Exception {
        userRep.deleteAll();
        rep.deleteAll();
        categoryRep.deleteAll();
        driver.close();

    }
}
