package com.jchartoire.mareu.service;

import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyApiService implements ApiService {

    private List<User> users = DummyGenerator.generateUsers();
    private List<Room> rooms = DummyGenerator.generateRooms();
    private List<Meeting> meetings = DummyGenerator.generateMeetings();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }

    public void createUser(User user) {
        users.add(user);
    }

    @Override
    public User getUserById(long Id) {
        for (User user : users) {
            if (user.getId() == Id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail() .equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public void deleteRoom(Room room) {
        rooms.remove(room);
    }

    public void createRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public Room getRoomById(long Id) {
        for (Room room : rooms) {
            if (room.getId() == Id) {
                return room;
            }
        }
        return null;
    }

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public void createMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    @Override
    public Meeting getMeetingById(long Id) {
        for (Meeting meeting : meetings) {
            if (meeting.getId() == Id) {
                return meeting;
            }
        }
        return null;
    }
}
