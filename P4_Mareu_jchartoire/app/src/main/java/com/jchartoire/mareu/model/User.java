package com.jchartoire.mareu.model;

/**
 * Model object representing a user
 */
public class User {

    private long id;
    private String name;
    private String firstName;
    private String email;

    /**
     * @param id        The unique ID of the user
     * @param name      The name of the user
     * @param firstName The firstname of the user
     * @param email     The email of the user
     */
    public User(long id, String firstName, String name, String email) {
        this.id = id;
        this.firstName = firstName;
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.name = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return (this.email);   // What to display in the Spinner or EditText.
    }
}
