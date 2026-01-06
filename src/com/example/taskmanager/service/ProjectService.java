package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    public boolean createProject(String name, int ownerId) {
        if (name == null || name.trim().isEmpty()) {
            return false; // UI tarafında mesaj gösterilecek
        }

        String sql = "INSERT INTO projects (name, owner_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, ownerId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false; // duplicate project veya hata
        }
    }

    public boolean deleteProject(int projectId, int ownerId) {
        // Önce projeye ait task’ları sil
        String deleteTasks = "DELETE FROM tasks WHERE project_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(deleteTasks)) {
            ps.setInt(1, projectId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Projeyi sil
        String sql = "DELETE FROM projects WHERE id=? AND owner_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setInt(2, ownerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Project> getProjectsByUser(int ownerId) {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE owner_id=? ORDER BY id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        ownerId
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Project getProjectById(int projectId) {
        String sql = "SELECT * FROM projects WHERE id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("owner_id")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean assignTaskToProject(int projectId, int taskId, int userId) {
        // Önce task mevcut mu ve başka projeye atanmış mı kontrol et
        String checkSql = "SELECT project_id FROM tasks WHERE id = ? AND user_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(checkSql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int existingProjectId = rs.getInt("project_id");
                if (existingProjectId != 0) {
                    return false; // UI tarafında mesaj gösterilecek
                }
            } else {
                return false; // task yok
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Task’ı projeye ata
        String sql = "UPDATE tasks SET project_id = ? WHERE id = ? AND user_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setInt(2, taskId);
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
