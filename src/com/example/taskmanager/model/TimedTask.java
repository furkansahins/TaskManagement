package com.example.taskmanager.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimedTask extends Task {

    private LocalDate deadline;

    public TimedTask(int id, String title, String description, Priority priority, User user, LocalDate deadline) {
        super(id, title, description, priority, user);
        this.deadline = deadline;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isOverdue() {
        return !completed && deadline.isBefore(LocalDate.now());
    }

    public long daysOverdue() {
        return ChronoUnit.DAYS.between(deadline, LocalDate.now());
    }

    public long daysLeft() {
        return java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(), deadline
        );
    }

    public boolean isUpcoming() {
        long days = daysLeft();
        return days >= 0 && days <= 7;
    }

    public boolean isUrgent() {
        long days = daysLeft();
        return days >= 0 && days <= 3;
    }

}
