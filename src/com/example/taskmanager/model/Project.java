package com.example.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private User owner;
    private List<Task> tasks;

    public Project(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        if (!tasks.contains(task)) {
            tasks.add(task);
        }
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}
