package com.example.taskmanager.model;

import java.time.LocalDate;

public class Notification {

    private Long id;
    private String message;
    private LocalDate createdAt;
    private boolean read;
    private User user;

    public Notification(Long id, String message, User user) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.createdAt = LocalDate.now();
        this.read = false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }
}
