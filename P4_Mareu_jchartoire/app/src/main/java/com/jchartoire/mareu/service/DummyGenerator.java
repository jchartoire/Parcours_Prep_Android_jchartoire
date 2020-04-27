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

public abstract class DummyGenerator {

    public static List<Room> dummyRooms = Arrays.asList(
            new Room(System.currentTimeMillis(), "Salle 1", 0xFFFC8E8E),
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
        //configure the first meeting for be always room1, 8:00, today.
        int dummyRoom = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        Date StartDate = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        Date EndDate = calendar.getTime();
        String largeString = "Lorem ipsum dolor sit amet\n\n" +
                "Togam orationem pudore existimatis aetatis esse castissima deductum.\n" +
                "quantum omnem illo Crassi omnem isti deductum erudiretur a orationem.\n" +
                "brevis hoc ut istam M ipsius a ad esse deinde.\n" +
                "Utilia multa reducere viam eum ut admovente veritatis.\n\n" +
                "veritatis cum viam admovente mariti stimulos reducere mariti ut similia.\n" +
                "mariti veritatis potius obstinatum.\n\n" +
                "► Quoniam mirari quosdam\n\n" +
                "► Proinde concepta rabie saeviore\n\n" +
                "► Novitates autem si spem adferunt\n\n" +
                "► Nisi mihi Phaedrum, inquam\n\n" +
                "► Ibi victu recreati et quiete\n\n";

        for (int i = 1; i <= 20; i++) {
            meeting = new Meeting(i, "Réunion " + i, dummyUsers.get(new Random().nextInt(9)), StartDate, EndDate,
                    dummyRooms.get(dummyRoom), Arrays.asList(dummyUsers.get(new Random().nextInt(9)),
                    dummyUsers.get(new Random().nextInt(9)), dummyUsers.get(new Random().nextInt(9)),
                    dummyUsers.get(new Random().nextInt(9))), largeString);
            dummyMeetings.add(meeting);
            calendar.add(Calendar.MINUTE, 30);
            if (calendar.get(Calendar.HOUR_OF_DAY) > 15) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                StartDate = calendar.getTime();
                calendar.add(Calendar.HOUR, 2);
                EndDate = calendar.getTime();
            } else {
                StartDate = calendar.getTime();
                calendar.add(Calendar.HOUR, 1);
                EndDate = calendar.getTime();
            }
            dummyRoom = new Random().nextInt(4);
        }
        return new ArrayList<>(dummyMeetings);
    }
}
