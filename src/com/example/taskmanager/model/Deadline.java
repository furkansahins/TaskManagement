package com.example.taskmanager.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Deadline {

    private final LocalDate date;

    public Deadline(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public long daysLeft() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

    public boolean isOverdue() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isUpcoming(int days) {
        long left = daysLeft();
        return left >= 0 && left <= days;
    }
}
