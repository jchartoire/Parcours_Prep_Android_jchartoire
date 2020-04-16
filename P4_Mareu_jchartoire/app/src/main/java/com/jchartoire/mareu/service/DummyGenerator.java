package com.jchartoire.mareu.service;

import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class DummyGenerator {

    private static List<Room> dummyRooms = Arrays.asList(
            new Room(1, "Salle 1", 0xFFFC8E8E),
            new Room(2, "Salle 2", 0xFFFCCB93),
            new Room(3, "Salle 3", 0xFFF1FC8E),
            new Room(4, "Salle 4", 0xFFABFC8E),
            new Room(5, "Salle 5", 0xFF8EFCF0),
            new Room(6, "Salle 6", 0xFF8EC7FC),
            new Room(7, "Salle 7", 0xFFAF8EFC),
            new Room(8, "Salle 8", 0xFFFC8EEE),
            new Room(9, "Salle 9", 0xFFD2D2D2),
            new Room(10, "Salle 10", 0xFF5A5A5A)
    );

    static List<Room> generateRooms() {
        return new ArrayList<>(dummyRooms);
    }

    private static List<User> dummyUsers = Arrays.asList(

            new User(1, "Aurore Nelson", "aurore.nelson@ie.com"),
            new User(2, "Adam Cook", "adam.cook@ie.com"),
            new User(3, "Alice Holland", "alice.holland@ie.com"),
            new User(4, "Axel Johnson", "axel.johnson@ie.com"),
            new User(5, "Amber Reed", "amber.reed@ie.com"),
            new User(6, "Allan Stone", "allan.stone@ie.com"),
            new User(7, "Anita Ferguson", "anita.ferguson@ie.com"),
            new User(8, "Antony Moore", "antony.moore@ie.com"),
            new User(9, "Anna Gordon", "anna.gordon@ie.com"),
            new User(10, "Albert Lee", "albert.lee@ie.com")
    );

    static List<User> generateUsers() {
        return new ArrayList<>(dummyUsers);
    }

    private static List<Meeting> dummyMeetings = Arrays.asList(
            new Meeting(1, "Réunion R&D", "Noah",
                    LocalDateTime.of(2020, 06, 01, 10, 15),
                    LocalDateTime.of(2020, 04, 14, 11, 30),
                    dummyRooms.get(1),
                    Arrays.asList(dummyUsers.get(1), dummyUsers.get(2), dummyUsers.get(3)),
                    "Réunion pour faire le point sur les avancées du projet X552"),
            new Meeting(2, "Réunion marketing", "Marie",
                    LocalDateTime.of(2020, 06, 02, 9, 30),
                    LocalDateTime.of(2020, 04, 12, 12, 00),
                    dummyRooms.get(3),
                    Arrays.asList(dummyUsers.get(4), dummyUsers.get(5), dummyUsers.get(6)),
                    "Réunion pour faire le point sur les ventes des 6 derniers mois"),
            new Meeting(3, "Réunion inventaire", "Damien",
                    LocalDateTime.of(2020, 06, 03, 14, 40),
                    LocalDateTime.of(2020, 04, 14, 16, 10),
                    dummyRooms.get(5),
                    Arrays.asList(dummyUsers.get(1), dummyUsers.get(3), dummyUsers.get(5)),
                    "Réunion pour faire le point sur l'inventaire de décembre"),
            new Meeting(4, "Réunion stagiaire", "Kelly",
                    LocalDateTime.of(2020, 06, 04, 15, 50),
                    LocalDateTime.of(2020, 04, 14, 17, 20),
                    dummyRooms.get(2),
                    Arrays.asList(dummyUsers.get(7), dummyUsers.get(8), dummyUsers.get(9)),
                    "Réunion pour attribuer les tâches aux nouveaux stagiaires")
    );

    static List<Meeting> generateMeetings() {
        return new ArrayList<>(dummyMeetings);
    }
}
