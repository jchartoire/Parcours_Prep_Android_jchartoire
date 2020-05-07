package com.jchartoire.mareu;

import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;
import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.service.DummyGenerator;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.jchartoire.mareu.tools.DateUtils.dateFormatter;
import static com.jchartoire.mareu.tools.DateUtils.getDateFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UnitTest {
    private ApiService apiService;
    private List<Meeting> dummyMeetings = DummyGenerator.dummyMeetings;
    private List<User> dummyUsers = DummyGenerator.dummyUsers;
    private List<Room> dummyRooms = DummyGenerator.dummyRooms;

    @BeforeEach
    void setup() {
        apiService = DI.getNewInstanceApiService();
    }

    //region MeetingAPI
    @Test
    @Order(1)
    @Tag("MeetingAPI")
    @DisplayName("Get Meetings list")
    void getMeetings() {
        List<Meeting> meetings = apiService.getMeetings();
        Matchers.containsInAnyOrder(meetings, dummyMeetings.toArray());
    }

    @Test
    @Order(2)
    @Tag("MeetingAPI")
    @DisplayName("Delete a meeting from the meetings list")
    void deleteMeeting() {
        Meeting meetingToDelete = apiService.getMeetings().get(0);
        assertTrue(apiService.getMeetings().contains(meetingToDelete));
        apiService.deleteMeeting(meetingToDelete);
        assertFalse(apiService.getMeetings().contains(meetingToDelete));
    }

    @Test
    @Order(3)
    @Tag("MeetingAPI")
    @DisplayName("Get a meeting by ID")
    void getMeetingById() {
        /* get a specific meeting from the list of Meetings by the service */
        Meeting dummyMeeting = dummyMeetings.get(0);
        Meeting meetingToTest = apiService.getMeetingById(dummyMeeting.getId());
        assertEquals(meetingToTest, dummyMeeting);
    }

    @Test
    @Order(4)
    @Tag("MeetingAPI")
    @DisplayName("Create new meeting")
    void createMeeting() {
        /* create a new meeting with just an ID, others details can remain empty */
        long newId = System.currentTimeMillis();
        Meeting newMeeting = new Meeting(newId, "", null, null, null, null, null, "");
        apiService.createMeeting(newMeeting);
        Meeting meetingToTest = apiService.getMeetingById(newId);
        assertEquals(newMeeting, meetingToTest);
    }

    @Test
    @Order(5)
    @Tag("MeetingAPI")
    @DisplayName("Get meeting's details")
    void getMeetingDetails() {
        // dummy detail of first meeting
        String dummyTitle = "dummyTitle";
        Room dummyRoom = dummyRooms.get(0);
        User dummyLeader = dummyUsers.get(0);
        List<User> dummyParticipants = Arrays.asList(dummyUsers.get(0), dummyUsers.get(1));
        Date dummyStartDate = getDateFor(2020, Calendar.JANUARY, 1, 8, 0);
        Date dummyEndDate = getDateFor(2020, Calendar.JANUARY, 1, 12, 0);
        String dummyString = "dummyString";

        // get first meeting by the service
        Meeting meetingToTest = apiService.getMeetingById(0);

        // assert all details are equals
        assertEquals(dummyTitle, meetingToTest.getTitle());
        assertEquals(dummyRoom, meetingToTest.getRoom());
        assertEquals(dummyLeader, meetingToTest.getLeader());
        assertEquals(dummyParticipants, meetingToTest.getUsers());
        assertEquals(dummyStartDate, meetingToTest.getStartDate());
        assertEquals(dummyEndDate, meetingToTest.getEndDate());
        assertEquals(dummyString, meetingToTest.getDescription());
    }

    @Test
    @Order(6)
    @Tag("MeetingAPI")
    @DisplayName("get list of meetings filtered by room, and by date")
    void filterMeetings() {
        /* compare dummyMeetings in room 1 count, with filtered list count */
        int meetingsInRoom = 0;
        Room roomToFilter = dummyRooms.get(0);
        for (Meeting meeting : dummyMeetings) {
            if (meeting.getRoom() == roomToFilter) {
                meetingsInRoom++;
            }
        }
        List<Meeting> meetingsFilteredByRoom = apiService.getFilteredMeetings(2, roomToFilter.getRoomName());
        assertEquals(meetingsInRoom, meetingsFilteredByRoom.size());

        /* compare dummyMeetings in room 1 count, with filtered list count */
        int meetingsToday = 0;
        String dateToFilter = dateFormatter.format(System.currentTimeMillis());
        for (Meeting meeting : dummyMeetings) {
            if (dateFormatter.format(meeting.getStartDate()).equals(dateToFilter)) {
                meetingsToday++;
            }
        }
        List<Meeting> meetingsFilteredByDate = apiService.getFilteredMeetings(1, dateToFilter);
        assertEquals(meetingsToday, meetingsFilteredByDate.size());
    }
    //endregion

    //region UserAPI
    @Test
    @Order(7)
    @Tag("UserAPI")
    @DisplayName("Get Users list")
    void getUsers() {
        List<User> users = apiService.getUsers();
        Matchers.containsInAnyOrder(users, dummyUsers.toArray());
    }

    @Test
    @Order(8)
    @Tag("UserAPI")
    @DisplayName("Get a user by email")
    void getUserByEmail() {
        /* get a specific user from the list of Users by the service */
        User dummyUser = dummyUsers.get(0);
        User userToTest = apiService.getUserByEmail(dummyUser.getEmail());
        assertEquals(userToTest, dummyUser);
    }

    @Test
    @Order(9)
    @Tag("UserAPI")
    @DisplayName("Get user's details")
    void getUserDetails() {
        /* get first user by the service */
        User userToTest = apiService.getUsers().get(0);
        /* assert that all details are get correctly */
        assertEquals("amy.hall@lamzone.com", userToTest.getEmail());
        assertEquals("Amy", userToTest.getFirstName());
        assertEquals("Hall", userToTest.getLastName());
    }
    //endregion

    //region RoomAPI
    @Test
    @Order(10)
    @Tag("RoomAPI")
    @DisplayName("Get room's details")
    void getRoomDetails() {
        /* get a dummy room of the generated list */
        Room dummyRoom = dummyRooms.get(0);
        /* get the corresponding room by the service */
        Room roomTotest = apiService.getRooms().get(0);
        /* assert that all details are get correctly */
        assertEquals(dummyRoom.getColor(), roomTotest.getColor());
        assertEquals(dummyRoom.getRoomName(), roomTotest.getRoomName());
    }
    //endregion
}