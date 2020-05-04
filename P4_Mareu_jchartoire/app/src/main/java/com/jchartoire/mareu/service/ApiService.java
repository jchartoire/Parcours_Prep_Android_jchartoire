package com.jchartoire.mareu.service;

import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;

import java.util.List;

/**
 * Neighbour API client
 */
public interface ApiService {

//region API Meeting

    /**
     * @return Return the list of meetings
     */
    List<Meeting> getMeetings();

    /**
     * @param Id The unique ID of the meeting
     * @return Return a Meeting
     */
    Meeting getMeetingById(long Id);

    /**
     * @param meeting object designating a meeting
     */
    void deleteMeeting(Meeting meeting);

    void createMeeting(Meeting meeting);

    /**
     * @return Return the list of filtered meetings
     */
    List<Meeting> getFilteredMeetings(int type, String param);//endregion
//region API User

    /**
     * @return Return a User
     */
    List<User> getUsers();

    /**
     * @param email The unique email of the user
     * @return Return a User
     */
    User getUserByEmail(String email);
//endregion

//region API Room

    /**
     * @return Return a Room
     */
    List<Room> getRooms();

    /**
     * @param Id The unique ID of the Room
     * @return Return a Room
     */
    Room getRoomById(long Id);
//endregion
}
