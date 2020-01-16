/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.deti.tqs.projetoapi.interfaces;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.junit.runner.RunWith;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import ua.deti.tqs.projetoapi.htmlpage.Index;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class IndexTest {

	@LocalServerPort
	private int port;
	
    private static WebDriver driver;
    private Index index;
    
    

    @Before
    public void setUp() throws Exception {
    	ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        index = new Index(this.driver, port);
    }

    @Test
    public void testTitle() throws Exception {
        Assert.assertEquals("Cloud Deliveries", index.getTitle());
    }
    
    @Test
    public void testSubTitle() throws Exception {
        Assert.assertEquals("your delivery service", index.getSubtitle());
    }

    @After
    public void tearDown() throws Exception {
        driver.close();

    }

}
