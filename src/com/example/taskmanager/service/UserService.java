package com.example.taskmanager.service;

import com.example.taskmanager.Database.DatabaseConnection;
import com.example.taskmanager.model.User;

import java.sql.*;
import java.util.regex.Pattern;

public class UserService {
    /**
     * Kullanıcının sisteme giriş yapmasını sağlar.
     *
     * @param username kullanıcı adı
     * @param password kullanıcı şifresi
     * @return giriş başarılıysa kullanıcı, değilse null
     */

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    public boolean isValidUsername(String username) {

        if (!VALID_USERNAME_PATTERN.matcher(username).matches()) {
            System.err.println("KULLANICI ADI GÜVENLİK UYARISI: Sadece harf, rakam, alt çizgi (_) ve tire (-) kullanabilirsiniz.");
            return false;
        }
        return true;
    }

    /**
     * Kullanıcının sisteme kayıt yapmasını sağlar.
     *
     * @param username kullanıcı adı
     * @param password kullanıcı şifresi
     * @return giriş başarılıysa kullanıcı, değilse null
     */
    public void register(String username, String password) {
        String sql = "INSERT INTO users(username,password) VALUES (?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
