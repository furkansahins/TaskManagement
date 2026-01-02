package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public User login(String username, String password) {

        String sql = """
            SELECT * FROM users
            WHERE username = ? AND password = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
