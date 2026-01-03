package com.example.taskmanager.model;

public class Task implements Completable {

    protected int id;
    protected String title;
    protected Priority priority;
    protected boolean completed;

    public Task(int id, String title, Priority priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.completed = completed;
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

    public String getTitle() {
        return title;
    }
    public Priority getPriority() {
        return priority;
    }

}
