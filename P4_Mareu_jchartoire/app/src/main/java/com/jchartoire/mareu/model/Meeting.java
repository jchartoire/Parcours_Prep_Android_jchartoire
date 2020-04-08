package com.jchartoire.mareu.model;

//todo:mentor est-ce qu'il faut un modèle pour les salles ?

/**
 * Model object representing a Neighbour
 */
public class Meeting {

    private int id;
    private String title;
    private String leader;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
    private String color;
    private String room;
    private String participant;
    private String description;

    //todo:mentor à quoi cela sert-il ?
    /**
     * Constructor
     * @param id
     * @param title;
     * @param leader;
     * @param startTime;
     * @param endTime;
     * @param startDate;
     * @param endDate;
     * @param color;
     * @param participant;
     * @param description;
     */
    public Meeting(int id, String title, String leader, String startTime, String endTime, String startDate, String endDate,
                   String color, String room, String participant, String description) {
        this.id = id;
        this.title = title;
        this.leader = leader;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.color = color;
        this.room = room;
        this.participant = participant;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
