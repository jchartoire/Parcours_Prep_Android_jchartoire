package com.jchartoire.mareu.model;

/**
 * Model object representing a room
 */
public class Room {

    private int id;
    private String roomName;
    private int color;

    /**
     * @param id       The unique ID of the room
     * @param roomName The name of the room
     * @param color    The color identifier of the room
     */
    public Room(int id, String roomName, int color) {
        this.id = id;
        this.roomName = roomName;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.roomName;            // What to display in the Spinner list.
    }
}
