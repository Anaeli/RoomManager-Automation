package test.java.tablet.schedule;

import static main.java.utils.AppConfigConstants.EXCEL_INPUT_DATA;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import main.java.pages.tablet.HomeTabletPage;
import main.java.pages.tablet.SchedulePage;
import main.java.rest.RoomManagerRestMethods;
import main.java.utils.readers.ExcelReader;

/**
 * TC23: Verify that the duration of the meeting can be changed by resizing the box in 
 * the timeline and clicking update
 * @author Jose Cabrera
 */
public class TheDurationOfTheMeetingIsChangedResizingInTimelineAndClickingUpdate {
	private HomeTabletPage homeTabletPage = new HomeTabletPage();
	private SchedulePage schedulePage;
	private ExcelReader excelReader = new ExcelReader(EXCEL_INPUT_DATA);
	private List<Map<String, String>> meetingData = excelReader.getMapValues("MeetingData");
	private String organizer = meetingData.get(1).get("Organizer");
	private String subject = meetingData.get(1).get("Subject");
	private String attendee = meetingData.get(1).get("Attendee");
	private String password = meetingData.get(1).get("Password");
	private String minStartTime = meetingData.get(1).get("Start time (minutes to add)");
	private String minEndTime = meetingData.get(1).get("End time (minutes to add)");
	
	@BeforeClass(groups = "UI")
	public void createNextMeeting() {
		schedulePage = homeTabletPage.clickScheduleBtn();
		schedulePage.createMeeting(organizer, subject, minStartTime, minEndTime, 
				attendee, password)
				.clickBackBtn();
	}
	
	@Test(groups = "UI")
	public void testMeetingDurationIsChangedResizingInTimelineAndClickingUpdate() {
		homeTabletPage.clickScheduleBtn();
		schedulePage.clickOverMeetingCreated(subject);
		String startTime = schedulePage.getStartTimeTxtBoxValue();
		schedulePage
		.moveTimelineAccordingToCurrentTime()
		.clickOverMeetingCreated(subject)
		.resizeMeetingLeft(subject)
		.clickUpdateBtn()
		.confirmCredentials(password);
		schedulePage.isMessageMeetingUpdatedDisplayed();
		Assert.assertFalse(schedulePage.getStartTimeTxtBoxValue().equals(startTime));
	}

	@AfterClass(groups = "UI")
	public void toHome() throws MalformedURLException, IOException {
		String roomName = meetingData.get(1).get("Room");
		RoomManagerRestMethods.deleteMeeting(roomName, subject, organizer + ":" + password);
		schedulePage.clickBackBtn();
	}
}
