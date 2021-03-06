package test.java.admin.resources;

import static main.java.utils.AppConfigConstants.EXCEL_INPUT_DATA;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import main.java.pages.admin.HomeAdminPage;
import main.java.pages.admin.resources.ResourceCreatePage;
import main.java.pages.admin.resources.ResourceDeletePage;
import main.java.pages.admin.resources.ResourcesPage;
import main.java.utils.readers.ExcelReader;

/**
 * TC01: Verify that a resource can be deleted.
 * @author Marco Llano
 */
public class ResourceCanBeDeleted {	
	@Test(groups = "ACCEPTANCE")
	public void testResourceCanBeDeleted() throws InterruptedException {	
		HomeAdminPage homeAdminPage = new HomeAdminPage();
		ResourcesPage resourcesPage = homeAdminPage.clickResourcesLink();
		ResourceCreatePage resourceCreatePage = resourcesPage.clickAddResourceBtn();
		
		//ExcelReader is used to read rooms data
		ExcelReader excelReader = new ExcelReader(EXCEL_INPUT_DATA);
		List<Map<String, String>> testData1 = excelReader.getMapValues("Resources");

		//Variable declaration and initialize
		String resourceName = testData1.get(2).get("ResourceName");
		String resourceDisplayName = testData1.get(2).get("ResourceDisplayName");
		String resourceDescription = testData1.get(2).get("Description");

		//Create new resource
		resourcesPage = resourceCreatePage.setResourceName(resourceName)
				.setResourceDisplayName(resourceDisplayName)
				.setResourceDescription(resourceDescription)
				.clickSaveResourceBtn();	

		//Delete created resource
		ResourceDeletePage resourceDeletePage = resourcesPage.selectResourceCheckbox(resourceName)
				.clickRemoveBtn();
		resourcesPage = resourceDeletePage.clickConfirmRemoveBtn();

		//Assertion for TC01
		Assert.assertFalse(resourcesPage.isResourceNameDisplayedInResourcesPage(resourceName));
	}
}