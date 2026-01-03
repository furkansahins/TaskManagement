package com.example.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private int id;
    private String name;
    private User owner;
    private List<Task> tasks = new ArrayList<>();
    public Project(int id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }
}
