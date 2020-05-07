package com.jchartoire.mareu.model;

import androidx.annotation.NonNull;

/**
 * Model object representing a user
 */
public class User {

    private long id;
    private String lastName;
    private String firstName;
    private String email;

    /**
     * @param id        The unique ID of the user
     * @param lastName  The lastName of the user
     * @param firstName The firstname of the user
     * @param email     The email of the user
     */
    public User(long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String toString() {
        return (this.email);   // What to display in the Spinner or EditText.
    }
}
