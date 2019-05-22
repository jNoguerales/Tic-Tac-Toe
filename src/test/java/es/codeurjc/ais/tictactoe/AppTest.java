package es.codeurjc.ais.tictactoe;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;

public class AppTest {
	WebDriver driver1;
	WebDriver driver2;
	WebDriverWait waitJaime;
	WebDriverWait waitBea;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		WebDriverManager.chromedriver().setup();
		WebApp.start();
	}
	
	@Before
	public void setUp() {
		driver1 = new ChromeDriver();
		driver1.get("http://localhost:8080");
		
		driver2 = new ChromeDriver();
		driver2.get("http://localhost:8080");
		
		waitJaime = new WebDriverWait(driver1,20);
		waitBea = new WebDriverWait(driver2,20);
		
		WebElement jaime = driver1.findElement(By.id("nickname"));
		jaime.sendKeys("Jaime");
		
		WebElement bea = driver2.findElement(By.id("nickname"));
		bea.sendKeys("Bea");
		
		
		WebElement button1 = driver1.findElement(By.id("startBtn"));
		WebElement button2 = driver2.findElement(By.id("startBtn"));
		button1.click();
		button2.click();
		
		waitJaime.until(ExpectedConditions.elementToBeClickable(By.id("cell-4")));
		WebElement cell4 = driver1.findElement(By.id("cell-4"));
		cell4.click();
		WebElement cell2 = driver2.findElement(By.id("cell-2"));
		cell2.click();
		WebElement cell0 = driver1.findElement(By.id("cell-0"));
		cell0.click();
	}
	
	@After
	public void teardown() {
		if (driver1 != null) {
			driver1.quit();
		}
		if (driver2 != null) {
			driver2.quit();
		}
	}
	
	@AfterClass 
	public static void teardownClass() {
		WebApp.stop();
	}
	
	@Test
	public void ganaPrimero() {
		WebElement cell5 = driver2.findElement(By.id("cell-5"));
		cell5.click();
		WebElement cell8 = driver1.findElement(By.id("cell-8"));
		cell8.click();
		
		waitJaime.until(ExpectedConditions.alertIsPresent());
		waitBea.until(ExpectedConditions.alertIsPresent());
		String texto1 = driver1.switchTo().alert().getText();
		String texto2 = driver2.switchTo().alert().getText();
		assertEquals(texto1,texto2);
		String ganador = texto1.substring(0, texto1.indexOf("wins")-1);
		assertEquals(ganador,"Jaime");
		String perdedor = texto1.substring(texto1.indexOf("wins") + 6, texto1.indexOf("looses")-1);
		assertEquals(perdedor,"Bea");
	}
	
	@Test
	public void pierdePrimero() {
		WebElement cell8 = driver2.findElement(By.id("cell-8"));
		cell8.click();
		WebElement cell3 = driver1.findElement(By.id("cell-3"));
		cell3.click();
		WebElement cell5 = driver2.findElement(By.id("cell-5"));
		cell5.click();
		
		waitJaime.until(ExpectedConditions.alertIsPresent());
		waitBea.until(ExpectedConditions.alertIsPresent());
		String texto1 = driver1.switchTo().alert().getText();
		String texto2 = driver2.switchTo().alert().getText();
		assertEquals(texto1,texto2);
		String ganador = texto1.substring(0, texto1.indexOf("wins")-1);
		assertEquals(ganador,"Bea");
		String perdedor = texto1.substring(texto1.indexOf("wins") + 6, texto1.indexOf("looses")-1);
		assertEquals(perdedor,"Jaime");
	}
	
	@Test
	public void Empate() throws InterruptedException {
		WebElement cell8 = driver2.findElement(By.id("cell-8"));
		cell8.click();
		WebElement cell6 = driver1.findElement(By.id("cell-6"));
		cell6.click();
		WebElement cell1 = driver2.findElement(By.id("cell-1"));
		cell1.click();
		WebElement cell5 = driver1.findElement(By.id("cell-5"));
		cell5.click();
		WebElement cell3 = driver2.findElement(By.id("cell-3"));
		cell3.click();
		WebElement cell7 = driver1.findElement(By.id("cell-7"));
		cell7.click();
		
		waitJaime.until(ExpectedConditions.alertIsPresent());
		waitBea.until(ExpectedConditions.alertIsPresent());
		String texto1 = driver1.switchTo().alert().getText();
		String texto2 = driver2.switchTo().alert().getText();
		assertEquals(texto1,texto2);
		assertEquals(texto1,"Draw!");
	}
}
