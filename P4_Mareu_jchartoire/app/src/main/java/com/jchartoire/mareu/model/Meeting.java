package com.jchartoire.mareu.model;


import java.util.Date;
import java.util.List;

/**
 * Model object representing a meeting
 */
public class Meeting {

    private long id;
    private String title;
    private User leader;
    private Date startDate;
    private Date endDate;
    private Room room;
    private List<User> users;
    private String description;

    /**
     * Constructor
     *
     * @param id          The unique ID of the meeting
     * @param title       The title of the meeting
     * @param leader      The leader who animate of the meeting
     * @param startDate   The starting date of the meeting
     * @param endDate     The ending date of the meeting
     * @param users       The list of the meeting participants
     * @param description A complete description of the meeting
     */
    public Meeting(long id, String title, User leader, Date startDate, Date endDate, Room room,
                   List<User> users, String description) {
        this.id = id;
        this.title = title;
        this.leader = leader;
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
        this.users = users;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
