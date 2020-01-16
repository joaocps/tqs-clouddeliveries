package ua.deti.tqs.projetoapi.interfac.user;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.bonigarcia.wdm.WebDriverManager;
import ua.deti.tqs.projetoapi.entities.ProductCategory;
import ua.deti.tqs.projetoapi.entities.User;
import ua.deti.tqs.projetoapi.repositories.OrderRep;
import ua.deti.tqs.projetoapi.repositories.ProductCategoryRep;
import ua.deti.tqs.projetoapi.repositories.UserRep;
import ua.deti.tqs.projetoapi.htmlpage.user.Profile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProfileTest {


	@LocalServerPort
	private int port;

	@Autowired
	OrderRep rep;

	@Autowired
	UserRep userRep;

	@Autowired
	ProductCategoryRep categoryRep;
	
	User user;

	private static WebDriver driver;
	private Profile profile;


	@Before
	public void setUp() throws Exception {
		userRep.deleteAll();
		rep.deleteAll();
		categoryRep.deleteAll();

		user = new User("test@ua.pt", "test");
		userRep.save(user);
		ProductCategory cat = new ProductCategory("category");
		categoryRep.save(cat);
		

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
		profile = new Profile(driver, port);

	}

	public void login(){
		driver.get(String.format("http://localhost:%d/login", port));
		driver.findElement(By.id("email")).sendKeys(user.getEmail());
		driver.findElement(By.id("password")).sendKeys(user.getPassword());
		driver.findElement(By.name("login-submit")).submit();
	}
	
	@Test
	public void num_rows(){
		login();
		driver.get(String.format("http://localhost:%d/userprofile", port));
		assertEquals(7, profile.numRows());
	}
	
	@Test
	public void goTo_changeuserprofile(){
		login();
		driver.get(String.format("http://localhost:%d/userprofile", port));
		profile.submit();
		assertTrue(driver.getCurrentUrl().contains("changeuserprofile"));
	}
	
	
	@After
	public void tearDown(){
		driver.close();
	}
}
