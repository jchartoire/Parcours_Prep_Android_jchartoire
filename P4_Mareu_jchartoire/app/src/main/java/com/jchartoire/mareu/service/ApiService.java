package com.jchartoire.mareu.service;

import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;

import java.util.List;

/**
 * Neighbour API client
 */
public interface ApiService {

    /**
     * users service
     */
    List<User> getUsers();

    /**
     * @param user
     */
    void deleteUser(User user);

    void createUser(User user);

    /**
     * @param Id The unique ID of the user
     * @return Return a User
     */
    User getUserById(int Id);

    /**
     * rooms service
     */
    List<Room> getRooms();

    /**
     * @param room
     */
    void deleteRoom(Room room);

    void createRoom(Room room);

    /**
     * @param Id The unique ID of the room
     * @return Return a Room
     */
    Room getRoomById(int Id);

    /**
     * meetings service
     */
    List<Meeting> getMeetings();

    /**
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    void createMeeting(Meeting meeting);

    /**
     * @param Id The unique ID of the meeting
     * @return Return a Meeting
     */
    Meeting getMeetingById(int Id);
}
