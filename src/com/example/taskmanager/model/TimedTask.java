package com.example.taskmanager.model;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimedTask extends Task {

    private LocalDate deadline;

    public TimedTask(int id,
                     String title,
                     Priority priority,
                     boolean completed,
                     LocalDate deadline) {

        super(id, title, priority, completed);
        this.deadline = deadline;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public long daysLeft() {
        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

    public boolean isOverdue() {
        return !completed && deadline.isBefore(LocalDate.now());
    }

    public long daysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(deadline, LocalDate.now());
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
