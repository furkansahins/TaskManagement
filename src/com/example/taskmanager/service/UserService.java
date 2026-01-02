package com.example.taskmanager.service;

import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users = new ArrayList<>();
    private User loggedInUser;
    private long idCounter = 1;

    public User register(String username, String password) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            return null;
        }

        User user = new User(idCounter++, username, password);
        users.add(user);
        return user;
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                    user.getPassword().equals(password)) {
                loggedInUser = user;
                return user;
            }
        }
        return null;
    }

    public void logout() {
        loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public List<User> getAllUsers() {
        return users;
    }
}
