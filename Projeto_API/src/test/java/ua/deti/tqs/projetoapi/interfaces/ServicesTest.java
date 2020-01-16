package ua.deti.tqs.projetoapi.interfaces;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.bonigarcia.wdm.WebDriverManager;
import ua.deti.tqs.projetoapi.htmlpage.ServicesPrices;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ServicesTest {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private ServicesPrices services;

    @Before
    public void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        services = new ServicesPrices(this.driver, port);
    }

    @Test
    public void login() {
        services.startNow();
        assertTrue(driver.getCurrentUrl().contains("sign_up"));
    }

    @After
    public void tearDown() throws Exception {
        driver.close();

    }

}
