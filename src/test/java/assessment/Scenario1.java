package assessment;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

public class Scenario1 {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		WebDriver driver = new EdgeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://www.flipkart.com/");
		driver.manage().window().maximize();
		
		//Handling login Popup, if popup is not displayed, it will wait for the time as passed in implicit wait then it goes to next step
		try {
			WebElement popUpClose = driver.findElement(By.xpath("//span[@role='button']"));
			if (popUpClose.isDisplayed()) {
				popUpClose.click();
			}
		} catch (Exception e) {
		}
		
		//Searching for "Bluetooth Speakers"
		driver.findElement(By.xpath("//input[@class='Pke_EE']")).sendKeys("Bluetooth Speakers", Keys.ENTER);
		
		//Applying filters
		driver.findElement(By.xpath("//div[text()='Brand']")).click();
		driver.findElement(By.xpath("//div[@title='boAt']")).click();
		WebElement ele = driver.findElement(By.xpath("//div[@title='4★ & above']"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,500);");
		Thread.sleep(1000);
		ele.click();
		Thread.sleep(1000);
		
		//Explicitly waiting for Price-High to Low sort by element and clicking on it
		WebElement priceLowToHigh = driver.findElement(By.xpath("//div[text()='Price -- Low to High']"));
		priceLowToHigh.click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.attributeContains(priceLowToHigh, "class", "dKeN6y"));
		
		//clicking on the 1st product
		List<WebElement> allProductName = driver.findElements(By.xpath("//div[@class='RGLWAk']"));
		allProductName.get(0).click();
		
		//Switching the driver control to second tab i.e., product description page
		String tab1Id = driver.getWindowHandle();
		Set<String> allIds = driver.getWindowHandles();
		for (String id : allIds) {
			if (!id.equals(tab1Id)) {
				driver.switchTo().window(id);
			}
		}
		Thread.sleep(1000);
		
		//validating "Available offers" is present, if present printing the total number of offers
		driver.findElement(By.xpath("//span[contains(text(),'offers')]")).click();
		WebElement availOffer = driver.findElement(By.xpath("//div[text()='Available offers']"));
		List<WebElement> offerLists = driver.findElements(By.xpath("//li[@class='Im3cwA col']"));
		if (availOffer.isDisplayed()) {
			System.out.println("Number of offers Available are: " + offerLists.size());
		} else {
			System.out.println("Available offers section doesnot exist");
		}
		
		// Scenario1 - When "Add To Cart" button is enabled, clicking on "Add To Cart" button and capturing the screenshot
		try {
			WebElement addToCart = driver.findElement(By.xpath("//button[text()='Add to cart']"));
			if (addToCart.isEnabled()) {
				addToCart.click();
				Thread.sleep(2000);
				takeScreenshot(driver, "cart_result");
			}
			// Scenario-2 - When "Add To Cart" button is missing or disabled, then capturing the webpage screenshot
			else {
				System.out.println("Product unavailable - Could not be added to cart");
				takeScreenshot(driver, "result");
			}
			// When "Out of Stock" is shown, then capturing the webpage screenshot
		} catch (Exception e) {
			System.out.println("Product unavailable - Could not be added to cart");
			takeScreenshot(driver, "result");
		}
		
		//Closing the browser
		driver.quit();

	}
	
	//Generic method to capture the screenshot of the webpage
	private static void takeScreenshot(WebDriver driver, String name) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File dest = new File(".\\Screenshots\\" + name + ".png");
		Files.copy(src, dest);
	}

}
