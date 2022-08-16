package Tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LambdaTest {
	public String username = "testyatest62";
	public String accesskey = "Sa4frXVX65QGb0YYowblzlSJswge2xYdujT4SEpbP6Uv1zQ2vA";
	public RemoteWebDriver driver = null;
	public String gridURL = "@hub.lambdatest.com/wd/hub";

	@BeforeTest
	@Parameters(value={"browser","version","platform","url"})
	public void setUp(String browser, String version, String platform, String url) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", browser);
		capabilities.setCapability("version", version);
		capabilities.setCapability("platform", platform); 
		capabilities.setCapability("build", "Lambdatest Grid Test");
		capabilities.setCapability("name", "Test scenarios");
		capabilities.setCapability("network", true);
		capabilities.setCapability("visual", true);
		capabilities.setCapability("video", true);
		capabilities.setCapability("console", true);
		try {
			driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + gridURL), capabilities);
		} catch (MalformedURLException e) {
			System.out.println("Invalid grid URL");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		driver.get(url);
		driver.manage().window().maximize();
	}
	
	@Test(timeOut = 40000)
	@Parameters(value="platform")
	public void TestScenario(String platform) {
		  WebElement IntegrationsLink = driver.findElement(By.xpath("(//a[contains(@href, 'integrations')])[2]"));
		  WebElement seamlessH2Text = driver.findElement(By.xpath("//h2[text()='Seamless Collaboration']"));
		  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		  wait.until(ExpectedConditions.visibilityOfAllElements(IntegrationsLink,seamlessH2Text));
		  JavascriptExecutor js = (JavascriptExecutor)driver;
		  js.executeScript("arguments[0].scrollIntoView();", IntegrationsLink);
		  js.executeScript("arguments[0].scrollIntoView();", seamlessH2Text);
		  String parentWindow = driver.getWindowHandle();
		  if(platform.equals("macOS Sierra")) {
			  String clickLink = Keys.chord(Keys.COMMAND,Keys.ENTER);
			  IntegrationsLink.sendKeys(clickLink);
		  }
		  else {
			  String clickLink = Keys.chord(Keys.CONTROL,Keys.ENTER);
			  IntegrationsLink.sendKeys(clickLink);
		  }
		  Set<String> window = driver.getWindowHandles();
		  List<String> wHandles = new ArrayList<String>(window);
		  System.out.println(wHandles);
		  driver.switchTo().window(wHandles.get(1));
		  String actualResult = driver.getCurrentUrl();
		  String expectedResult = "https://www.lambdatest.com/integrations";
		  Assert.assertEquals(actualResult,expectedResult);
		  
		  WebElement codelessAutomationText = driver.findElement(By.cssSelector("div[id='codeless_row'] h2"));
		  js.executeScript("arguments[0].scrollIntoView();", codelessAutomationText);
		  driver.findElement(By.xpath("//a[contains(@href,'testingwhiz')]")).click();
		  //String expectedTitle = "TestingWhiz Integration | LambdaTest";
		  String expectedTitle = "Running Automation Tests Using TestingWhiz LambdaTest | LambdaTest";
		  String actualTitle = driver.getTitle();
		  Assert.assertEquals(actualTitle, expectedTitle);
		  driver.close();
		  System.out.println("current window count: "+driver.getWindowHandles().size());
		  driver.switchTo().window(parentWindow);
		  driver.get("https://www.lambdatest.com/blog");
		  WebElement communityLink = driver.findElement(By.xpath("//a[@href='https://community.lambdatest.com/'][contains(text(),'Community')]"));
		  communityLink.click();
		  String actualURL= driver.getCurrentUrl();
		  String expectedURL = "https://community.lambdatest.com/";
		  Assert.assertEquals(actualURL, expectedURL);
	  }
	  @AfterTest
	  public void afterTest() {
		  if(driver != null) {
		  driver.quit();
		  }
	  }
	
}
