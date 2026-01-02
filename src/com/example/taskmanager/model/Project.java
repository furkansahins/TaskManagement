package com.example.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private Long id;
    private String name;
    private String description;
    private List<Task> tasks;

    public Project(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}
