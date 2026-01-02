package com.example.taskmanager.model;

public class Task implements Completable {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Priority priority;
    private User user;

    public Task(Long id, String title, String description, Priority priority, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.completed = false;
    }

    public Long getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    @Override
    public void complete() {
        this.completed = true;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
