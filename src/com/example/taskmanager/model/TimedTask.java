package com.example.taskmanager.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimedTask extends Task {

    private LocalDate deadline;

    public TimedTask(String title,
                     String description,
                     Priority priority,
                     User user,
                     LocalDate deadline) {

        super(title, description, priority, user);
        this.deadline = deadline;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isOverdue() {
        return !isCompleted() && deadline.isBefore(LocalDate.now());
    }

    public long daysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(deadline, LocalDate.now());
    }
}
