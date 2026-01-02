package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    public Project createProject(String name, User owner) {
        String sql = "INSERT INTO projects (name, owner_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setInt(2, owner.getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Project(rs.getInt(1), name, owner);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> getProjectsByUser(User user) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE owner_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                projects.add(new Project(rs.getInt("id"), rs.getString("name"), user));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    public void addTaskToProject(Project project, Task task) {
        String sql = "UPDATE tasks SET project_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, project.getId());
            ps.setInt(2, task.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(Project project) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, project.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasksInProject(Project project) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, project.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate deadline = rs.getDate("deadline") != null ? rs.getDate("deadline").toLocalDate() : null;
                User user = new User(rs.getInt("user_id"), "", "");
                if (deadline != null) {
                    tasks.add(new TimedTask(rs.getInt("id"), rs.getString("title"),
                            rs.getString("description"),
                            Priority.valueOf(rs.getString("priority")),
                            user, deadline));
                } else {
                    tasks.add(new Task(rs.getInt("id"), rs.getString("title"),
                            rs.getString("description"),
                            Priority.valueOf(rs.getString("priority")), user));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
