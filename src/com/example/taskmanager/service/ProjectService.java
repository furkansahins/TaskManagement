package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 * Proje işlemlerini yöneten servis sınıfıdır.
 */
public class ProjectService {
    /**
     * Yeni bir proje oluşturur.
     *
     * @param name proje adı
     * @param ownerId projeyi oluşturan kullanıcı
     */
    public void createProject(String name, int ownerId) {

        String sql = "INSERT INTO projects(name, owner_id) VALUES (?, ?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, ownerId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Belirtilen projeyi siler.
     *
     * @param projectId silinecek proje
     * @param ownerId proje sahibi
     * @return silme başarılıysa true
     */
    public boolean deleteProject(int projectId, int ownerId) {
        String sql = "DELETE FROM projects WHERE id=? AND owner_id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setInt(2, ownerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
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

    /**
     * Bir görevi projeye atar.
     *
     * @param projectId proje id
     * @param taskId görev id
     * @param userId kullanıcı id
     * @return işlem başarılıysa true
     */
    public boolean assignTaskToProject(int projectId, int taskId, int userId) {
        String sql = """
        UPDATE tasks
        SET project_id = ?
        WHERE id = ? AND user_id = ?
    """;

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
