package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final TaskService taskService = new TaskService();

    public void checkAndCreateUrgentNotifications(int userId) {

        List<TimedTask> tasks = taskService.getUpcomingTimedTasks(userId);

        for (TimedTask task : tasks) {

            if (task.isUrgent()) {

                String message =
                        "URGENT: \"" + task.getTitle() +
                                "\" deadline in " + task.daysLeft() + " days.";

                if (!notificationExists(message, userId)) {
                    createNotification(message, userId);
                }
            }
        }
    }

    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> list = new ArrayList<>();

        String sql = """
            SELECT * FROM notifications
            WHERE user_id = ? AND is_read = false
            ORDER BY created_at DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getBoolean("is_read"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = true WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createNotification(String message, int userId) {
        String sql = """
            INSERT INTO notifications (user_id, message, is_read, created_at)
            VALUES (?, ?, false, NOW())
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean notificationExists(String message, int userId) {
        String sql = """
            SELECT 1 FROM notifications
            WHERE user_id = ? AND message = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }
}

