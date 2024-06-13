package webScraper;
//All of this is for pressing buttons to view the full list of 1000 people.
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//Jsoup will be the library we use to read html and actual do the scraping
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//BufferedWriter is what we will use to output a csv file with the desired information
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
public class main{
	public static void main(String[] args) {
		//creates a scraper to use the scrape function later.
		Scraper crawl = new Scraper();
		//lets webdriver use our chromedriver applications
		//Change the second path to the path connecting to chromedriver on your device can be downloaded here https://googlechromelabs.github.io/chrome-for-testing/#stable
		System.setProperty("webdriver.chrome.driver", "C:/Users/billy/Downloads/chromedriver-win32/chromedriver-win32/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		//lets us run chromeium in the background
		options.addArguments("--headless");
		//lets our driver know to not show any ui and run it in the back
		WebDriver driver = new ChromeDriver(options);
		try {
			driver.get(crawl.URL);
			//waits for the website to load
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			//This establishes the show all button for our code and presses it
			WebElement showAllButton = driver.findElement(By.id("ctl01_TemplateBody_WebPartManager1_gwpciDirectory_ciDirectory_Findpeople_lstSearchResults_Grid1_ctl00_ctl02_ctl00_ShowAll"));
			showAllButton.click();
			// waits for the changes to occur
			Thread.sleep(3000);
			//We have the scraper take in a document doc here so it takes the one showing all people and not the one that shows only 10
			Document doc = Jsoup.parse(driver.getPageSource());
			//stops the driver
			driver.quit();
			crawl.scrape(doc);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			//stops driver no matter what happens in the end
			driver.quit();
		}
	}
}
