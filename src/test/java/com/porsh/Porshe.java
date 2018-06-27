package com.porsh;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Porshe {

	public static void varification(String stepText, double expected, double actual) {
		System.out.println(" Testing: " + stepText + " ");

		if ((int) expected == (int) actual) {
			System.out.println("*Pass");
		} else {
			System.out.println("## Fail");
			System.out.println("Expected: " + expected);
			System.out.println("Actual  : " + actual);
		}
		System.out.println();
	}

	public static double parseCurrency(String str) {
		return Double
				.parseDouble(str.substring(str.indexOf("$") + 1, str.lastIndexOf("0") + 1).replaceAll(",", "").trim());

	}

	public static void main(String[] args) throws InterruptedException {
		// 1.open the browser
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		// 2.got to url
		driver.get("https://www.porsche.com/usa/modelstart/");

		// 3.searching for model
		WebElement search = driver.findElement(By.xpath("(//div[@class = 'b-teaser-caption'])[1]"));
		search.click();

		// 4.saving the price
		WebElement webElement = driver.findElement(By.id("m982120")).findElement(By.xpath("//div[1]/div[2]/div[2]"));
		String str = webElement.getText();

		double savedPrice = parseCurrency(str);
		System.out.println(savedPrice);

		// *Store the current window handle
		String winHandleBefore = driver.getWindowHandle();

		// 5.Click on Build & Price under 718 Cayman
		driver.findElement(By.xpath("(//a[@class = 'm-01-link m-14-build'])[1]")).click();

		// 6. Verify that Base price displayed on the page is same as the price from
		// step 4
		Iterator<String> iterator = driver.getWindowHandles().iterator();
		String subWindowHandler = null;
		while (iterator.hasNext()) {
			subWindowHandler = iterator.next();
		}
		// * switch to popup window
		driver.switchTo().window(subWindowHandler);
		Thread.sleep(1000);
		// second page Base price

		webElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[1]/div[2]"));
		String str2 = webElement.getText();
		double basePrice = parseCurrency(str2);

		// compare if prices are the same
		varification("If saved price is the same as base price", savedPrice, basePrice);

		// 7. Verify that Price for Equipment is 0

		webElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]"));
		String str3 = webElement.getText();
		double equipPriceForZero = parseCurrency(str3);
		double expectedPriceForZero = 0.0;
		varification("If price for equipment is 0", expectedPriceForZero, equipPriceForZero);

		// 8. Verify that total price is the sum of base price + Delivery, Processing
		// and Handling Fee delivery
		webElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[3]/div[2]"));
		String str4 = webElement.getText();
		double deliveryPrice = parseCurrency(str4);

		// sum deliv+base
		double sum = basePrice + deliveryPrice;

		// total
		webElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]"));
		String str5 = webElement.getText();
		double totalPrice = parseCurrency(str5);

		varification("If  total price is  sum of delivery+base ", totalPrice, sum);

		// Perform the actions on new window
		// 9.Select color “Miami Blue”
		driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_FJ5\"]/span")).click();

		// 10.Verify that Price for Equipment is Equal to Miami Blue price

		double equipPrice = parseCurrency(
				driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		varification("If Price for Equipment is Equal to Miami Blue price", 2580, equipPrice);

		// 11.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery,Processing and Handling Fee

		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		varification("if total price is the sum of base price + Price for Equipment + Delivery",
				basePrice + deliveryPrice + equipPrice, totalPrice);

		// 12. Select 20" Carrera Sport Wheels
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		new WebDriverWait(driver, 10)
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#s_exterieur_x_MXRD")));
		executor.executeScript("arguments[0].click();", driver.findElement(By.cssSelector("#s_exterieur_x_MXRD")));
		

		// 13.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport Wheels
		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		webElement = driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_IAF\"]/div[2]/div[1]/div/div[2]"));
		String st = webElement.getText();
		double color = parseCurrency(st);

		webElement = driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_IRA\"]/div[2]/div[1]/div/div[2]"));
		String ss = webElement.getText();
		double wheel = parseCurrency(ss);

		double summ = wheel + color;
		varification("If Price for Equipment is the sum of Miami Blue price + 20\" Carrera Sport Wheels", summ,
				equipPrice);

		// 14.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery, Processing and Handling Fee
		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		varification("if total price is the sum of base price + Price for Equipment + Delivery",
				basePrice + deliveryPrice + equipPrice, totalPrice);

		// 15.Select seats ‘Power Sport Seats (14-way) with Memory Package’
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("s_interieur_x_PP06")));
		

		// 16.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Spor Wheels + Power Sport Seats (14-way) with Memory Package
		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());

		webElement = driver.findElement(By.xpath("//*[@id=\"seats_73\"]/div[2]/div[1]/div[3]/div"));
		String seat = webElement.getText();
		double seats = parseCurrency(seat);
		summ = summ + seats;
		varification(
				" If Price for Equipment is the sum of Miami Blue price + 20\" Carrera Spor Wheels + Power Sport Seats (14-way) with Memory Package ",
				equipPrice, summ);
		// 17.Verify that total price is the sum of base price + Price for Equipment +
		// Delivery, Processing and Handling Fee
		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());

		varification("If  total price is the sum of base price + Price for Equipment + Delivery", totalPrice,
				basePrice + equipPrice + deliveryPrice);

		// 18.Click on Interior Carbon Fiber
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("IIC_subHdl")));
		

		// 19.Select Interior Trim in Carbon Fiber i.c.w. Standard Interior
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("vs_table_IIC_x_PEKH_x_c01_PEKH")));

		// 20.Verify that Price for Equipment is the sum of Miami Blue price + 20" Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package +Interior Trim in Carbon Fiber i.c.w. Standard Interior
		 equipPrice = parseCurrency(
		      driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		 webElement=driver.findElement(By.xpath("//*[@id=\"vs_table_IIC_x_PEKH\"]/div[1]/div[2]"));
		 String inter=webElement.getText();
		 double interior=parseCurrency(inter);
		//summ=summ+interior;
		 varification("If Price for Equipment is the sum of Miami Blue price + 20 Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package + Interior Trim in Carbon Fiber i.c.w. Standard Interior ",equipPrice,summ+interior );
		
		 
		// 21.Verify that total price is the sum of base price + Price for Equipment +  Delivery,Processing and Handling Fee
		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		varification("If  total price is the sum of base price + Price for Equipment + Delivery", totalPrice,basePrice + equipPrice + deliveryPrice);

		//22.Click on Performance
		executor.executeScript("arguments[0].click();",driver.findElement(By.id("IMG_subHdl")));
		
		//23.Select 7-speed Porsche Doppelkupplung (PDK)
		executor.executeScript("arguments[0].click();",driver.findElement(By.id("vs_table_IMG_x_M250_x_c14_M250_x_shorttext")));
		
		//24.Select Porsche Ceramic Composite Brakes (PCCB)
		executor.executeScript("arguments[0].click();",driver.findElement(By.id("vs_table_IMG_x_M450_x_c91_M450")));
		
		
		//25.Verify that Price for Equipment is the sum of Miami Blue price + 20" Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package + Interior Trim in Carbon Fiber i.c.w. Standard Interior + 7-speed Porsche Doppelkupplung (PDK) + Porsche Ceramic Composite Brakes (PCCB)
		equipPrice = parseCurrency(
			      driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		
		webElement=driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M250\"]/div[1]/div[2]/div"));
		String priceBrakes=webElement.getText();
		double breaks=parseCurrency(priceBrakes);
		
		webElement=driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M450\"]/div[1]/div[2]/div"));
		String sstr=webElement.getText();
		double ceramicBrakes=parseCurrency(sstr);
		
		varification("Price for Equipment is the sum of Miami Blue price + 20\" Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package + Interior Trim in Carbon Fiber i.c.w. Standard Interior + 7-speed Porsche Doppelkupplung (PDK) + Porsche Ceramic Composite Brakes (PCCB)",equipPrice,summ+breaks+ceramicBrakes);
		//26.Verify that total price is the sum of base price + Price for Equipment + Delivery, Processing and Handling Fee
		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		varification("If  total price is the sum of base price + Price for Equipment + Delivery", totalPrice,basePrice + equipPrice + deliveryPrice);
		// Close the new window, if that window no more required
		 driver.close();

		// Switch back to original browser (first window)
		 driver.switchTo().window(winHandleBefore);

		// Continue with original browser (first window)

		
		driver.quit();

	}

}
