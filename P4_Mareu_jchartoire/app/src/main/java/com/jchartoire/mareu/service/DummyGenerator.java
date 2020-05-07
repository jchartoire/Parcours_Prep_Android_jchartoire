package com.jchartoire.mareu.service;

import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.jchartoire.mareu.tools.DateUtils.getDateFor;

public abstract class DummyGenerator {

    public static List<Room> dummyRooms = Arrays.asList(
            new Room(1, "Salle 1", 0xFFFC8E8E),
            new Room(2, "Salle 2", 0xFF8EC7FC),
            new Room(3, "Salle 3", 0xFFFCCB93),
            new Room(4, "Salle 4", 0xFFAF8EFC),
            new Room(5, "Salle 5", 0xFFF1FC8E),
            new Room(6, "Salle 6", 0xFFFC8EEE),
            new Room(7, "Salle 7", 0xFFABFC8E),
            new Room(8, "Salle 8", 0xFFD2D2D2),
            new Room(9, "Salle 9", 0xFF8EFCF0),
            new Room(10, "Salle 10", 0xFF5A5A5A)
    );
    public static List<User> dummyUsers = Arrays.asList(
            new User(1, "Amy", "Hall", "amy.hall@lamzone.com"),
            new User(2, "Adam", "Cook", "adam.cook@lamzone.com"),
            new User(3, "Alice", "Shaw", "alice.shaw@lamzone.com"),
            new User(4, "Axel", " Ortiz", "axel.ortiz@lamzone.com"),
            new User(5, "Amber", "Reed", "amber.reed@lamzone.com"),
            new User(6, "Allan", "Stone", "allan.stone@lamzone.com"),
            new User(7, "Anita", "Gray", "anita.gray@lamzone.com"),
            new User(8, "Antony", "Moore", "antony.moore@lamzone.com"),
            new User(9, "Anna", "Gordon", "anna.gordon@lamzone.com"),
            new User(10, "Albert", "Lee", "albert.lee@lamzone.com")
    );
    public static List<Meeting> dummyMeetings = new ArrayList<>();

    static List<Room> generateRooms() {
        return new ArrayList<>(dummyRooms);
    }

    static List<User> generateUsers() {
        return new ArrayList<>(dummyUsers);
    }

    static List<Meeting> generateMeetings() {
        Meeting meeting;
        // add the first meeting with static data
        meeting = new Meeting(0, "dummyTitle", dummyUsers.get(0),
                getDateFor(2020, Calendar.JANUARY, 1, 8, 0),
                getDateFor(2020, Calendar.JANUARY, 1, 12, 0),
                dummyRooms.get(0), Arrays.asList(dummyUsers.get(0),
                dummyUsers.get(1)), "dummyString");
        dummyMeetings.add(meeting);

        //configure the next meeting starting at 8am today.
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dummyStartDate = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date dummyEndDate = calendar.getTime();
        String dummyString = "Lorem ipsum dolor sit amet\n\n" + "► Line 1\n\n" + "► Line 2\n\n" + "► Line 3\n\n" + "► Line 4\n\n" +
                "► Line 5\n\n" + "► Line 6\n\n" + "► Line 7\n\n" + "► Line 8\n\n" + "► Line 9\n\n" + "► Line 10\n\n";

        // generate random meetings
        for (int i = 1; i <= 20; i++) {
            Room dummyRoom = dummyRooms.get(new Random().nextInt(4));
            User dummyLeader = dummyUsers.get(new Random().nextInt(10));
            List<User> dummyParticipants = Arrays.asList(dummyUsers.get(new Random().nextInt(10)),
                    dummyUsers.get(new Random().nextInt(10)),
                    dummyUsers.get(new Random().nextInt(10)));

            meeting = new Meeting(i, "Réunion " + i, dummyLeader, dummyStartDate, dummyEndDate, dummyRoom, dummyParticipants, dummyString);
            dummyMeetings.add(meeting);

            calendar.add(Calendar.MINUTE, 30);
            if (calendar.get(Calendar.HOUR_OF_DAY) > 15) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                dummyStartDate = calendar.getTime();
                calendar.add(Calendar.HOUR, 2);
                dummyEndDate = calendar.getTime();
            } else {
                dummyStartDate = calendar.getTime();
                calendar.add(Calendar.HOUR, 1);
                dummyEndDate = calendar.getTime();
            }
        }
        return new ArrayList<>(dummyMeetings);
    }
}
