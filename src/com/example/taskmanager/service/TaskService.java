package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    public void createTask(String title, String description, Priority priority, User user) {
        String sql = "INSERT INTO tasks (title, description, priority, user_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, priority.name());
            ps.setInt(4, user.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTimedTask(String title, String description, Priority priority, User user, LocalDate deadline) {
        String sql = "INSERT INTO tasks (title, description, priority, user_id, deadline) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, priority.name());
            ps.setInt(4, user.getId());
            ps.setDate(5, java.sql.Date.valueOf(deadline));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasksByUser(User user) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate deadline = rs.getDate("deadline") != null ? rs.getDate("deadline").toLocalDate() : null;
                if (deadline != null) {
                    tasks.add(new TimedTask(rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            Priority.valueOf(rs.getString("priority")),
                            user,
                            deadline));
                } else {
                    tasks.add(new Task(rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            Priority.valueOf(rs.getString("priority")),
                            user));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void completeTask(Task task) {
        String sql = "UPDATE tasks SET completed = true WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, task.getId());
            ps.executeUpdate();
            task.complete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Task task) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, task.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Task> getTasksByProject(int projectId) {

        List<Task> tasks = new ArrayList<>();

        String sql = """
        SELECT * FROM tasks
        WHERE project_id = ?
        ORDER BY id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LocalDate deadline =
                        rs.getDate("deadline") != null
                                ? rs.getDate("deadline").toLocalDate()
                                : null;

                User user = new User(rs.getInt("user_id"), "", "");

                if (deadline != null) {
                    tasks.add(new TimedTask(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            Priority.valueOf(rs.getString("priority")),
                            user,
                            deadline
                    ));
                } else {
                    tasks.add(new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            Priority.valueOf(rs.getString("priority")),
                            user
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public List<TimedTask> getUpcomingTimedTasks(User user) {
        List<Task> tasks = getTasksByUser(user);
        List<TimedTask> upcoming = new ArrayList<>();

        for (Task task : tasks) {
            if (task instanceof TimedTask timedTask) {
                if (!task.isCompleted() && timedTask.isUpcoming()) {
                    upcoming.add(timedTask);
                }
            }
        }
        return upcoming;
    }

}
