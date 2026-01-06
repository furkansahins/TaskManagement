package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    /**
     * Kullanıcıya ait yeni bir görev oluşturur.
     *
     * @param title görev başlığı
     * @param priority görev önceliği (null olabilir)
     * @param userId görevi oluşturan kullanıcı
     */

    public void createTask(String title, Priority priority, int userId) {
        String sql = """
        INSERT INTO tasks (title, priority, completed, user_id)
        VALUES (?, ?, false, ?)
    """;

        if (priority == null) {
            priority = Priority.LOW; // ✅ DEFAULT
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, priority.name());
            ps.setInt(3, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Zaman sınırlı (deadline'lı) bir görev oluşturur.
     *
     * @param title görev başlığı
     * @param priority görev önceliği
     * @param userId kullanıcı id
     * @param deadline görevin son tarihi
     */
    public void createTimedTask(String title,
                                Priority priority,
                                int userId,
                                LocalDate deadline) {

        String sql = """
        INSERT INTO tasks (title, priority, completed, user_id, deadline)
        VALUES (?, ?, false, ?, ?)
    """;

        if (priority == null) {
            priority = Priority.LOW; // ✅ DEFAULT
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, priority.name());
            ps.setInt(3, userId);
            ps.setDate(4, Date.valueOf(deadline));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tasks.add(mapTask(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public List<Task> getTasksByProject(int projectId) {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE project_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tasks.add(mapTask(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Belirtilen görevi tamamlandı olarak işaretler.
     *
     * @param taskId tamamlanacak görev id
     * @param userId işlemi yapan kullanıcı
     * @return işlem başarılıysa true, değilse false
     */
    public boolean completeTask(int taskId, int userId) {
        String sql = """
            UPDATE tasks
            SET completed = true
            WHERE id = ? AND user_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Belirtilen görevi siler.
     *
     * @param taskId silinecek görev
     * @param userId işlemi yapan kullanıcı
     * @return silme başarılıysa true
     */
    public boolean deleteTask(int taskId, int userId) {
        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public List<TimedTask> getUpcomingTimedTasks(int userId) {
        List<Task> tasks = getTasksByUserId(userId);
        List<TimedTask> upcoming = new ArrayList<>();

        for (Task task : tasks) {
            if (task instanceof TimedTask timedTask) {
                if (timedTask.getDeadline().isUpcoming(7)) {
                    upcoming.add(timedTask);
                }
            }
        }
        return upcoming;
    }

    private Task mapTask(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String title = rs.getString("title");
        Priority priority = rs.getString("priority") != null
                ? Priority.valueOf(rs.getString("priority"))
                : Priority.LOW;

        boolean completed = rs.getBoolean("completed");

        Date sqlDeadline = rs.getDate("deadline");

        if (sqlDeadline != null) {
            return new TimedTask(
                    id,
                    title,
                    priority,
                    completed,
                    new Deadline(sqlDeadline.toLocalDate())
            );
        }

        return new Task(id, title, priority, completed);
    }
    /**
     * Bir projeye ait görevleri TXT dosyası olarak dışa aktarır.
     *
     * @param projectId proje id
     * @param projectName proje adı
     * @return oluşturulan dosyanın tam yolu
     */
    public String exportProjectTasksToTxt(int projectId, String projectName) {

        List<Task> tasks = getTasksByProject(projectId);

        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";

        String fileName = projectName.replaceAll("\\s+", "_") + ".txt";
        String fullPath = desktopPath + File.separator + fileName;

        try (FileWriter writer = new FileWriter(fullPath)) {

            writer.write("=== PROJECT: " + projectName + " ===\n\n");

            if (tasks.isEmpty()) {
                writer.write("No tasks.\n");
            }

            for (Task task : tasks) {

                writer.write(task.getId() + " | " + task.getTitle());

                if (task.getPriority() != null) {
                    writer.write(" | " + task.getPriority());
                }

                if (task instanceof TimedTask timedTask) {
                    writer.write(" | Deadline: " + timedTask.getDeadline());

                    if (timedTask.isOverdue()) {
                        writer.write(" | OVERDUE");
                    }
                }

                if (task.isCompleted()) {
                    writer.write(" | DONE");
                }

                writer.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullPath;
    }





}
