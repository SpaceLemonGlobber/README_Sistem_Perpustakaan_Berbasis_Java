package com.perpus.app.dao;

import com.perpus.app.models.User;
import com.perpus.app.models.Admin;
import com.perpus.app.models.Anggota;
import com.perpus.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /* ===============================
       LOGIN
       =============================== */
    public User login(String username, String password) {
        String sql =
            "SELECT user_id, username, password, role " +
            "FROM user " +
            "WHERE username = ? AND password = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String role = rs.getString("role");

                if ("ADMIN".equalsIgnoreCase(role)) {
                    return new Admin(id, username);
                } else {
                    return new Anggota(id, username);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ===============================
       GET USER BY ID
       =============================== */
    public User getById(int id) {
        String sql =
            "SELECT user_id, username, role " +
            "FROM user " +
            "WHERE user_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String username = rs.getString("username");

                if ("ADMIN".equalsIgnoreCase(role)) {
                    return new Admin(id, username);
                } else {
                    return new Anggota(id, username);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
