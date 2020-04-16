package com.jchartoire.mareu.model;

/**
 * Model object representing a user
 */
public class User {

    private int id;
    private String name;
    private String email;

    /**
     * @param id    The unique ID of the user
     * @param name  The name of the user
     * @param email The email of the user
     */
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return (this.name + " <" + this.email + ">");            // What to display in the Spinner list.
    }
}
