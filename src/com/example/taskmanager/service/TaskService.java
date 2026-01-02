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

    // NORMAL TASK
    public Task createTask(String title, String description,
                           Priority priority, User user) {

        if (title == null || title.isBlank()) {
            return null;
        }

        String sql = """
            INSERT INTO tasks (title, description, priority, completed, user_id)
            VALUES (?, ?, ?, false, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, priority.name());
            ps.setInt(4, user.getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Task(
                        rs.getInt(1),
                        title,
                        description,
                        priority,
                        user
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // TIMED TASK
    public TimedTask createTimedTask(String title, String description,
                                     Priority priority, User user,
                                     LocalDate deadline) {

        if (title == null || title.isBlank() || deadline == null) {
            return null;
        }

        String sql = """
            INSERT INTO tasks
            (title, description, priority, completed, user_id, deadline)
            VALUES (?, ?, ?, false, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, priority.name());
            ps.setInt(4, user.getId());
            ps.setDate(5, java.sql.Date.valueOf(deadline));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new TimedTask(
                        rs.getInt(1),
                        title,
                        description,
                        priority,
                        user,
                        deadline
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // USER'A GÖRE TASK LİSTELE
    public List<Task> getTasksByUser(User user) {

        List<Task> tasks = new ArrayList<>();

        String sql = """
            SELECT * FROM tasks
            WHERE user_id = ?
            ORDER BY id
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LocalDate deadline =
                        rs.getDate("deadline") != null
                                ? rs.getDate("deadline").toLocalDate()
                                : null;

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

    // TASK COMPLETE
    public void completeTask(Task task) {

        String sql = """
            UPDATE tasks
            SET completed = true
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, task.getId());
            ps.executeUpdate();
            task.complete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // OVERDUE KONTROL
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
        return java.time.temporal.ChronoUnit.DAYS
                .between(task.getDeadline(), LocalDate.now());
    }
}
