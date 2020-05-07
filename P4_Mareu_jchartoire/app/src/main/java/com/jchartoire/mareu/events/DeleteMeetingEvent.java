package com.jchartoire.mareu.events;

import com.jchartoire.mareu.model.Meeting;

/**
 * Event fired when a user deletes a Neighbour
 */
public class DeleteMeetingEvent {

    /**
     * Meeting to delete
     */
    public Meeting meeting;

    /**
     * Constructor.
     *
     * @param meeting Model object representing a meeting
     */
    public DeleteMeetingEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}
