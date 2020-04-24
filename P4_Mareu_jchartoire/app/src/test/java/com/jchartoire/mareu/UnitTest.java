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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitTest {
    private ApiService apiService;
    private List<Meeting> dummyMeetings = DummyGenerator.dummyMeetings;
    private List<User> dummyUsers = DummyGenerator.dummyUsers;
    private List<Room> dummyRooms = DummyGenerator.dummyRooms;

    @BeforeEach
    void setup() {
        apiService = DI.getNewInstanceApiService();
    }

    @Test
    @Order(1)
    @Tag("MeetingAPI")
    @DisplayName("Get Meetings list with success")
    void getMeetings() {
        List<Meeting> meetings = apiService.getMeetings();
        Matchers.containsInAnyOrder(meetings, dummyMeetings.toArray());
    }

    @Test
    @Order(2)
    @Tag("MeetingAPI")
    @DisplayName("Delete a meeting from the meetings list with success")
    void deleteMeeting() {
        Meeting meetingToDelete = apiService.getMeetings().get(0);
        apiService.deleteMeeting(meetingToDelete);
        assertFalse(apiService.getMeetings().contains(meetingToDelete));
    }

    @Test
    @Order(3)
    @Tag("MeetingAPI")
    @DisplayName("Get a meeting by ID with success")
    void getMeetingById() {
        /* get a specific meeting from the list of Meetings by the service */
        Meeting dummyMeeting = dummyMeetings.get(1);
        Meeting meetingToTest = apiService.getMeetingById(dummyMeeting.getId());
        assertEquals(meetingToTest, dummyMeeting);
    }

    @Test
    @Order(4)
    @Tag("MeetingAPI")
    @DisplayName("Create new meeting with success")
    void createMeeting() {
        /* create a new meeting with just an ID, others details can remain empty */
        long newId = System.currentTimeMillis();
        Meeting newMeeting = new Meeting(newId, "", null, null, null, null, null, null);
        apiService.createMeeting(newMeeting);
        Meeting meetingToTest = apiService.getMeetingById(newId);
        assertEquals(newMeeting, meetingToTest);
    }

    @Test
    @Order(5)
    @Tag("MeetingAPI")
    @DisplayName("Get meeting's details with success")
    void getMeetingDetails() {
        /* get a dummy meeting of the generated list */
        Meeting dummyMeeting = dummyMeetings.get(1);
        /* get the corresponding meeting by the service */
        Meeting meetingTotest = apiService.getMeetings().get(1);
        /* assert that all details are get correctly */
        assertEquals(dummyMeeting.getId(), meetingTotest.getId());
        assertEquals(dummyMeeting.getTitle(), meetingTotest.getTitle());
        assertEquals(dummyMeeting.getRoom(), meetingTotest.getRoom());
        assertEquals(dummyMeeting.getUsers(), meetingTotest.getUsers());
        assertEquals(dummyMeeting.getStartDate(), meetingTotest.getStartDate());
        assertEquals(dummyMeeting.getEndDate(), meetingTotest.getEndDate());
        assertEquals(dummyMeeting.getLeader(), meetingTotest.getLeader());
        assertEquals(dummyMeeting.getDescription(), meetingTotest.getDescription());
    }

    @Test
    @Order(6)
    @Tag("UserAPI")
    @DisplayName("Get Users list with success")
    void getUsers() {
        List<User> users = apiService.getUsers();
        Matchers.containsInAnyOrder(users, dummyUsers.toArray());
    }

    @Test
    @Order(7)
    @Tag("UserAPI")
    @DisplayName("Delete a user from the users list with success")
    void deleteUser() {
        User userToDelete = apiService.getUsers().get(0);
        apiService.deleteUser(userToDelete);
        assertFalse(apiService.getUsers().contains(userToDelete));
    }

    @Test
    @Order(8)
    @Tag("UserAPI")
    @DisplayName("Get a user by email with success")
    void getUserByEmail() {
        /* get a specific user from the list of Users by the service */
        User dummyUser = dummyUsers.get(1);
        User userToTest = apiService.getUserByEmail(dummyUser.getEmail());
        assertEquals(userToTest, dummyUser);
    }

    @Test
    @Order(9)
    @Tag("UserAPI")
    @DisplayName("Get a user by ID with success")
    void getUserById() {
        /* get a specific user from the list of Users by the service */
        User dummyUser = dummyUsers.get(1);
        User userToTest = apiService.getUserById(dummyUser.getId());
        assertEquals(userToTest, dummyUser);
    }

    @Test
    @Order(10)
    @Tag("UserAPI")
    @DisplayName("Create a user with success")
    void createUser() {
        /* create a new user with just an ID, others details can remain empty */
        long newId = System.currentTimeMillis();
        User newUser = new User(newId, "", "", "");
        apiService.createUser(newUser);
        User userToTest = apiService.getUserById(newId);
        assertEquals(newUser, userToTest);
    }

    @Test
    @Order(11)
    @Tag("UserAPI")
    @DisplayName("Get user's details with success")
    void getUserDetails() {
        /* get a dummy user of the generated list */
        User dummyUser = dummyUsers.get(1);
        /* get the corresponding user by the service */
        User userTotest = apiService.getUsers().get(1);
        /* assert that all details are get correctly */
        assertEquals(dummyUser.getId(), userTotest.getId());
        assertEquals(dummyUser.getEmail(), userTotest.getEmail());
        assertEquals(dummyUser.getFirstName(), userTotest.getFirstName());
    }

    @Test
    @Order(12)
    @Tag("RoomAPI")
    @DisplayName("Delete a room from the rooms list with success")
    void deleteRoom() {
        Room roomToDelete = apiService.getRooms().get(0);
        apiService.deleteRoom(roomToDelete);
        assertFalse(apiService.getRooms().contains(roomToDelete));
    }

    @Test
    @Order(13)
    @Tag("RoomAPI")
    @DisplayName("Get a room by ID with success")
    void getRoomById() {
        /* get a specific room from the list of Rooms by the service */
        Room dummyRoom = dummyRooms.get(1);
        Room roomToTest = apiService.getRoomById(dummyRoom.getId());
        assertEquals(roomToTest, dummyRoom);
    }

    @Test
    @Order(14)
    @Tag("RoomAPI")
    @DisplayName("Create a room with success")
    void createRoom() {
        /* create a new room with just an ID, others details can remain empty */
        long newId = System.currentTimeMillis();
        Room newRoom = new Room(newId, "", 0);
        apiService.createRoom(newRoom);
        Room roomToTest = apiService.getRoomById(newId);
        assertEquals(newRoom, roomToTest);
    }

    @Test
    @Order(15)
    @Tag("RoomAPI")
    @DisplayName("Get room's details with success")
    void getRoomDetails() {
        /* get a dummy room of the generated list */
        Room dummyRoom = dummyRooms.get(1);
        /* get the corresponding room by the service */
        Room roomTotest = apiService.getRooms().get(1);
        /* assert that all details are get correctly */
        assertEquals(dummyRoom.getId(), roomTotest.getId());
        assertEquals(dummyRoom.getColor(), roomTotest.getColor());
        assertEquals(dummyRoom.getRoomName(), roomTotest.getRoomName());
    }
}