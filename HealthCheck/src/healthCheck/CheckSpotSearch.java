package healthCheck;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.generics.spotsearch.BaseClass;
public class CheckSpotSearch extends BaseClass {

	public String URL;
	public String Question;
	public int browser;
	public int intentNo;
	public int websiteNo;
	public String locator;
	@Test(priority = 1)
	public void description()
	{
		Scanner sc = new Scanner(System.in);
		Reporter.log("Hi, Welcome to Automation framework", true);
		Reporter.log("Please select the Website number and hit enter "+"\n"+"1. WSU- Alive/Orientaton"+"\n"+"2. WSU- Enrollment Management"+"\n"+"3. WSU-Registrar"+"\n"+"4. ICC"+"\n"+"5. USC"+"\n"+"6. SS Main Account",true);
		websiteNo= sc.nextInt();
		Reporter.log("Type the Question and hit enter ",true);
		sc.nextLine();
		Question= sc.nextLine(); 
		//Selecting browser
		Reporter.log("Enter the browser number and hit enter: "+"\n"+"1.Google Chrome"+"\n"+"2.Microsoft Edge"+"\n"+"3.Mozilla firefox",true);
		browser= sc.nextInt();
	}

	@Test (priority = 2)
	public void testSS1() throws EncryptedDocumentException, IOException, InterruptedException
	{
		//fetching URL for the selected website from Excel Sheet
		URL=getExcelData("website",websiteNo ,2);
		//fetching SS icon locator for the selected website from Excel Sheet
		locator=getExcelData("website",websiteNo,3);
		openBrowser(browser);
		openWebsite(URL);
		checkIntent(Question, locator);
		int columns= checkMenuBar();
		Reporter.log("Total number of columns is: "+columns,true);
		checkContent(websiteNo);
	}
	@Test(priority = 3)
	public void testSS2() throws InterruptedException, IOException
	{
		checkSocialMedia(websiteNo);
		checkLanguage(websiteNo);
		checkPoweredBySiaIcon();
		closeBrowser();
	}
}

























//		int count=lists.size();
//		if(count==3)
//		{
//			checkDefinition(Question);
//			checkInSiteContext();
//			checkDocument();
//		}
//		else if (count==2)
//		{
//			checkDefinition(Question);
//			checkInSiteContext();
//		}
//		else if(count==1)
//		{
//			checkInSiteContext();
//		}

//		for(String list : list)
//		{
//			if(list.equalsIgnoreCase("DEFINITION"))
//			{
//				checkDefinition(Question);
//			}
//			else if(list.equalsIgnoreCase("IN-SITE CONTEXT"))
//			{
//				checkInSiteContext();
//			}
//			else if(list.equalsIgnoreCase("DOCUMENTS"))
//			{
//				//checkDocument();
//			}



