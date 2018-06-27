import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Price {

	public static void main(String[] args) {
WebDriverManager.chromedriver().setup();
		
		WebDriver driver = new ChromeDriver();
		
		driver.get("https://cc.porsche.com/icc_pcna/ccCall.do?rt=1529368160&screen=1280x800&userID=USM&lang=us&PARAM=parameter_internet_us&ORDERTYPE=982120&CNR=C02&MODELYEAR=2019&hookURL=https%3a%2f%2fwww.porsche.com%2fusa%2fmodelstart%2fall%2f");
		
		 WebElement Base = driver.findElement(By.xpath("//(div[@class='ccaLabel'])[1]"));
			System.out.println(Base.getCssValue("(div[class='ccaLabel'])[1]"));
			

	}

}
