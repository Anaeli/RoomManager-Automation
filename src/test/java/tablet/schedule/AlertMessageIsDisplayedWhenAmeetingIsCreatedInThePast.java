package test.java.tablet.schedule;


import static main.java.utils.AppConfigConstants.EXCEL_INPUT_DATA;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import main.java.pages.tablet.HomeTabletPage;
import main.java.pages.tablet.SchedulePage;
import main.java.rest.RoomManagerRestMethods;
import main.java.utils.readers.ExcelReader;

/**
 * TC46: This test case verifies that an alert message is displayed 
 * when the user tries to create a meeting in the past.
 * @author Eliana Navia
 *
 */
public class AlertMessageIsDisplayedWhenAmeetingIsCreatedInThePast {
	private Logger log = Logger.getLogger(getClass());
	private ExcelReader excelReader = new ExcelReader(EXCEL_INPUT_DATA);
	private List<Map<String, String>> meetingData = excelReader.getMapValues("MeetingData");

	private String organizer = meetingData.get(11).get("Organizer");
	private String subject = meetingData.get(11).get("Subject");
	private String password = meetingData.get(11).get("Password");
	private String room = meetingData.get(1).get("Room");
	private String authentication = organizer + ":" + password;

	@Test (groups = "FUNCTIONAL")
	public void testAlertMessageIsDisplayedWhenAmeetingIsCreatedInThePast() {
		HomeTabletPage homeTabletPage = new HomeTabletPage();
		SchedulePage schedulePage = homeTabletPage.clickScheduleBtn();
		schedulePage.createMeeting(organizer, subject, 
				meetingData.get(11).get("Start time (minutes to add)"), 
				meetingData.get(11).get("End time (minutes to add)"), 
				meetingData.get(11).get("Attendee"), password);

		Assert.assertTrue(schedulePage.isMessageInformationOfPastMeetingDisplayed());
	}

	/**
	 * Delete meeting by rest API.
	 */
	@AfterClass (groups = "FUNCTIONAL")
	public void end() {
		PropertyConfigurator.configure("log4j.properties");
		try {
			RoomManagerRestMethods.deleteMeeting(room, subject, authentication);
			log.info("The meeting:" + subject + "has been deleted successfully.");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
