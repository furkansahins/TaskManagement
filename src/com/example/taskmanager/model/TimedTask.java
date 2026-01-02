package com.example.taskmanager.model;

import java.time.LocalDate;

public class TimedTask extends Task {

    private LocalDate deadline;

    public TimedTask(int id,
                     String title,
                     String description,
                     Priority priority,
                     User user,
                     LocalDate deadline) {

        super(id, title, description, priority, user);
        this.deadline = deadline;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
}
