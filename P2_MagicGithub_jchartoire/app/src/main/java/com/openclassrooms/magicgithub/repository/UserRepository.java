package com.openclassrooms.magicgithub.repository;

import com.openclassrooms.magicgithub.api.ApiService;
import com.openclassrooms.magicgithub.model.User;

import java.util.List;


public class UserRepository {

    private final ApiService apiService;

    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<User> getUsers() {
        // TODO_ok: A modifier
        //return null;
        return apiService.getUsers();
    }

    public void generateRandomUser() {
        // TODO_ok: A modifier
        apiService.generateRandomUser();
    }

    public void deleteUser(User user) {
        // TODO_ok: A modifier
        apiService.deleteUser(user);
    }
}
