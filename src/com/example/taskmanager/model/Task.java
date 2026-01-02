package com.example.taskmanager.model;

public class Task implements Completable {

    private String title;
    private String description;
    private Priority priority;
    private boolean completed;
    private User user;

    public Task(String title, String description, Priority priority, User user) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void complete() {
        this.completed = true;
    }
}
