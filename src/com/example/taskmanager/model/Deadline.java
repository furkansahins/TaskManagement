package com.example.taskmanager.model;

import java.time.LocalDate;

public class Deadline {

    private LocalDate date;

    public Deadline(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
