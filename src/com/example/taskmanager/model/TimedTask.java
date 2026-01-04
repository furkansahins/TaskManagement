package com.example.taskmanager.model;

public class TimedTask extends Task {

    private Deadline deadline;

    public TimedTask(int id,
                     String title,
                     Priority priority,
                     boolean completed,
                     Deadline deadline) {

        super(id, title, priority, completed);
        this.deadline = deadline;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public long daysLeft() {
        return deadline.daysLeft();
    }

    public boolean isOverdue() {
        return !completed && deadline.isOverdue();
    }

    public boolean isUpcoming() {
        return !completed && deadline.isUpcoming(7);
    }

    public boolean isUrgent() {
        return !completed && deadline.isUpcoming(3);
    }
}
