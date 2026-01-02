package com.example.taskmanager.model;

public class Task implements Completable {

    protected int id;
    protected String title;
    protected String description;
    protected Priority priority;
    protected boolean completed;
    protected User user;

    public Task(int id, String title, String description,
                Priority priority, User user) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.completed = false;
    }

    @Override
    public void complete() {
        this.completed = true;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
