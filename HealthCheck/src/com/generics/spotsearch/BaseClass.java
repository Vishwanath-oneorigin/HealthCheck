package com.generics.spotsearch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.examples.util.TempFileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;

public class BaseClass {
	static
	{
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "./driver/geckodriver.exe");
		System.setProperty("webdriver.edge.driver", "./driver/msedgedriver.exe");
	}
	public WebDriver driver;

	/**
	 * Generic method to open the browser
	 * @param int browser
	 */
	public void openBrowser(int browser)
	{
		if(browser==1) {
			driver = new ChromeDriver();
		}

		else if(browser==2) {
			driver = new EdgeDriver();
		}

		else if(browser==3) {
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
	}

	/**
	 * Generic method to open the website
	 * @param String websiteUrl
	 */
	public void openWebsite(String websiteUrl)
	{
		driver.get(websiteUrl);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
	}

	public void checkIntent(String intent,String locator)
	{
		driver.findElement(By.xpath(""+locator+"")).click();
		driver.findElement(By.xpath("//input[@id='spotsearch']")).sendKeys(intent+Keys.ENTER);
	}
	
	/**
	 * Generic method to find the page name of a newly opened tab
	 * Closes the newly opened tab after fetching the 'Page name'
	 */
	public String findPageName() throws InterruptedException
	{
		String pwh = driver.getWindowHandle();
		Set<String> whs = driver.getWindowHandles();
		for(String wh : whs)
		{	
			if(wh!=pwh)
			{
				driver.switchTo().window(wh);
			}
		}
		Thread.sleep(1000);
		String pageName=driver.getTitle();
		driver.close();
		driver.switchTo().window(pwh);		
		return pageName;
	}
	/**
	 * Generic method to check the contents in the menu bar
	 * returns the total number of columns/sections present
	 */
	public int checkMenuBar()
	{
		List<WebElement> allList = driver.findElements(By.xpath("//div[@class='ss-main-menu']/div"));
		int count = allList.size();
		return count;
	}
	/**
	 * Generic method to check the contents under each section
	 * Validates each option's heading, content heading and links
	 * Takes Screenshots of each option
	 */
	public void checkContent(int websiteNo) throws InterruptedException, IOException
	{	
		String columnName=null;
		String pgName=null;
		int a,b,c;
		c=0;
		int count=checkMenuBar();

		for(b=1;b<=count;b++)
		{
			boolean doc=false;
			driver.findElement(By.xpath("//div[@class='ss-main-menu']/div["+b+"]")).click();
			columnName= driver.findElement(By.xpath("//div[@class='ss-main-menu']/div["+b+"]")).getText();
			takeSS(websiteNo,"SectionsSS",columnName+" column");
			Reporter.log("\n"+"Column name is: "+"'"+columnName+"'",true);
			try
			{
				driver.findElement(By.xpath("//span[contains(.,'View all')]")).click();
			}
			catch(Exception e) {}

			for(a=1;a<100;a++)
			{
				if(b>1)
				{
					a=1+c;
				}
				try
				{
					driver.findElement(By.xpath("(//span[@class='ss-result-item'])["+a+"]")).click();
					String optionName= driver.findElement(By.xpath("(//span[@class='ss-result-item'])["+a+"]")).getText();
					takeSS(websiteNo,"ColumnContentsSS",a+".option");
					String contentHeadingName= driver.findElement(By.xpath("(//h3[@class='result_header2'])["+a+"]")).getText();
					//To check the link
					driver.findElement(By.xpath("(//a[contains(.,'Read More')])["+a+"]")).click();

					if(columnName.equalsIgnoreCase("DOCUMENTS"))
					{
						doc=true;
						pgName="PDF FILE";
					}
					if(doc==false)
					{	
						pgName=findPageName();
					}
					Reporter.log("\n"+"Option Name is: "+"'"+optionName+"' , "+"Content heading name is: "+"'"+contentHeadingName+"'",true);
					Reporter.log("Name of the page displayed after clicking on View More is: "+"'"+pgName+"'",true);
				}
				catch(Exception e)
				{
					break;
				}
				c++;
			}
		}
	}

	/**
	 * Generic method to check/validate Social Media handles
	 * returns Name and ID of each Social Media handle
	 * Takes Screenshot of each Social Media handle
	 */
	public void checkSocialMedia(int websiteNo) throws IOException, InterruptedException
	{
		boolean s=true;
		try {
			driver.findElement(By.xpath("//div[@class='ss-social social_media']")).click();
		}
		catch(Exception e)
		{
			s=false;
		}

		System.out.println(s);
		if(s==true)
		{
			Reporter.log("\n"+"Socail Media handles are present", true);
			boolean t=driver.findElement(By.xpath("//div[@data-handle='twitter']")).isDisplayed();
			if(t==true)
			{
				driver.findElement(By.xpath("//div[@data-handle='twitter']")).click();
				String tName=driver.findElement(By.xpath("//span[@class='social_title']")).getText();
				String tID= driver.findElement(By.xpath("//div[@class='social_handle-heading']")).getText();
				Reporter.log("Social Media Name is- "+"'"+tName+"' "+"and Social Media ID is-"+"'"+tID+"'",true);
				takeSS(websiteNo,"Social Media handles SS", tName);
			}
			else
				Reporter.log("Twitter Media handle is not present",true);

			boolean f=driver.findElement(By.xpath("//div[@data-handle='facebook']")).isDisplayed();
			if(f==true)
			{
				driver.findElement(By.xpath("//div[@data-handle='facebook']")).click();
				String fName=driver.findElement(By.xpath("(//span[@class='social_title'])[2]")).getText();
				String fID= driver.findElement(By.xpath("(//div[@class='social_handle-heading'])[2]")).getText();
				Reporter.log("Social Media Name is- "+"'"+fName+"' "+"and Social Media ID is-"+"'"+fID+"'",true);
				takeSS(websiteNo,"Social Media handles SS", fName);
			} 
			else
				Reporter.log("Facebook Media handle is not present",true);
		}
		else
			Reporter.log("\n"+"No Social Media handles are present", true);
	}

	/**
	 * Generic method to check the languages present in the menu bar
	 * notifies different types of languages present
	 * Takes Screenshot of the content in each language
	 */
	public void checkLanguage(int websiteNo) throws InterruptedException, IOException
	{
		boolean b=true;
		try {
			driver.findElement(By.xpath("//p[@class='lang-heading']/span[contains(@id,'lang')]")).click();
		}
		catch(Exception e)
		{
			b=false;
		}
		if(b==true)
		{
			Set<String> s= new HashSet<String>();
			List<WebElement> langs = driver.findElements(By.xpath("//p[@class='lang-heading']/span[contains(@id,'lang')]"));
			for(int i=1; i<=langs.size();i++)
			{
				driver.findElement(By.xpath("(//p[@class='lang-heading']/span[contains(@id,'lang')])["+i+"]")).click();
				String lang= driver.findElement(By.xpath("(//p[@class='lang-heading']/span[contains(@id,'lang')])["+i+"]")).getText();
				s.add(lang);
				Thread.sleep(3000);
				takeSS(websiteNo,"languages SS",lang);
			}
			driver.findElement(By.xpath("(//p[@class='lang-heading']/span[contains(@id,'lang')])[1]")).click();
			Reporter.log("\n"+"Languages present are- "+s,true);
		}
		else
			Reporter.log("\n"+"Language options are NOT present", true);
	}

	/**
	 * Generic method to check 'PoweredBySia' icon
	 * Validates whether the icon is present and clickable or not
	 * fetches the 'page name' displayed after clicking on that icon
	 */
	public void checkPoweredBySiaIcon() throws InterruptedException
	{
		boolean b=true;
		try {
			driver.findElement(By.xpath("//a[contains(.,'powered by')]")).click();
		}
		catch(Exception e)
		{
			b=false;
		}
		if(b==true)
		{
			driver.findElement(By.xpath("//a[contains(.,'powered by')]")).click();
			String pgname=findPageName();
			Reporter.log("\n"+"After clicking on the 'Powered By Sia' icon, it takes us to the page- "+pgname,true);
		}
		else
			Reporter.log("\n"+"Powered By Sia Icon is NOT present",true);
	}
	/**
	 * Generic method to take Screenshots
	 * Stores all the Screenshots according to the folder name and file name(png format)
	 * Overrides files with same name
	 */
	public void takeSS(int websiteNo,String folderName, String fileName) throws IOException, InterruptedException
	{
		Thread.sleep(2000);
		TakesScreenshot t = (TakesScreenshot) driver;
		File src=t.getScreenshotAs(OutputType.FILE);
		File dest = new File("./"+websiteNo+folderName+"/"+fileName+".png");
		FileUtils.copyFile(src, dest);
		Thread.sleep(1500);
	}
	/**
	 * Generic method to fetch data from Excel sheet
	 */
	public String getExcelData(String sheetname, int rownum, int cellnum) throws EncryptedDocumentException, IOException
	{
		FileInputStream fis = new FileInputStream("./data/data.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		String value= wb.getSheet(sheetname).getRow(rownum).getCell(cellnum).toString();
		return value;	
	}
	/**
	 * Generic method to write data to Excel sheet
	 */
	public void setExcelData(String sheetname, int rownum, int cellnum, String value) throws EncryptedDocumentException, IOException
	{
		FileInputStream fis = new FileInputStream("./data/data.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		wb.getSheet(sheetname).getRow(rownum).getCell(cellnum).setCellValue(value);
		FileOutputStream fos = new FileOutputStream("./data/data.xlsx");
		wb.write(fos);
		wb.close();
	}
	/**
	 * Generic method to close all the windows opened
	 */
	public void closeBrowser()
	{
		driver.quit();
	}
}
