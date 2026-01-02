package com.example.taskmanager.service;

import com.example.taskmanager.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private List<Task> tasks = new ArrayList<>();
    private long idCounter = 1;

    public Task createTask(String title, String description,
                           Priority priority, User user) {

        if (title == null || title.isBlank()) {
            return null;
        }

        Task task = new Task(idCounter++, title, description, priority, user);
        tasks.add(task);
        return task;
    }

    public TimedTask createTimedTask(String title, String description,
                                     Priority priority, User user,
                                     LocalDate deadline) {

        if (title == null || title.isBlank() || deadline == null) {
            return null;
        }

        TimedTask task = new TimedTask(
                idCounter++, title, description, priority, user, deadline
        );
        tasks.add(task);
        return task;
    }

    public List<Task> getTasksByUser(User user) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getUser().equals(user)) {
                result.add(task);
            }
        }
        return result;
    }

    public void completeTask(Task task) {
        task.complete();
    }

    // 🔥 OVERDUE LOGIC
    public boolean isOverdue(TimedTask task) {
        if (task.isCompleted()) {
            return false;
        }
        return task.getDeadline().isBefore(LocalDate.now());
    }

    public long overdueDays(TimedTask task) {
        if (!isOverdue(task)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(task.getDeadline(), LocalDate.now());
    }
}
