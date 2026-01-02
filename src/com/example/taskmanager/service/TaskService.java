package com.example.taskmanager.service;

import com.example.taskmanager.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private final List<Task> tasks = new ArrayList<>();

    public Task createTask(String title, String description,
                           Priority priority, User user) {

        if (title == null || title.isBlank()) return null;

        Task task = new Task(title, description, priority, user);
        tasks.add(task);
        return task;
    }

    public TimedTask createTimedTask(String title, String description,
                                     Priority priority, User user,
                                     LocalDate deadline) {

        if (title == null || title.isBlank() || deadline == null) return null;

        TimedTask task = new TimedTask(
                title, description, priority, user, deadline
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

    public void deleteTask(Task task) {
        tasks.remove(task);
    }
}
