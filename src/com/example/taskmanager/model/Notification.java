package com.example.taskmanager.model;

import java.time.LocalDateTime;

public class Notification {

    private int id;
    private int userId;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    public Notification(int id, int userId, String message,
                        boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Notification(int userId, String message) {
        this.userId = userId;
        this.message = message;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    // getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }

    public void markAsRead() {
        this.read = true;
    }
}
