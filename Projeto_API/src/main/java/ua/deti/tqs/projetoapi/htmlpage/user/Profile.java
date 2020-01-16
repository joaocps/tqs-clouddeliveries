package ua.deti.tqs.projetoapi.htmlpage.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ua.deti.tqs.projetoapi.htmlpage.HTMLPage;

public class Profile extends HTMLPage{

	public Profile(WebDriver driver, int port) {
		super(driver, String.format("http://localhost:%d/userprofile", port));
		
	}
	
	public void submit(){
		driver.findElement(By.id("submit")).click();
	}
	
	public int numRows(){
		return driver.findElements(By.className("row")).size();
	}

}
