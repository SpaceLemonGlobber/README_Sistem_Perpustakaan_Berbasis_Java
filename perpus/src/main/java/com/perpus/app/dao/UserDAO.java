package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.perpus.app.models.Admin;
import com.perpus.app.models.Anggota;
import com.perpus.app.models.User;
import com.perpus.config.Database;

public class UserDAO {

    public User login(String username, String password) {
        // Gunakan userId sesuai image_0225ee.png
        String sql = "SELECT userId, username, password, role FROM user WHERE username = ? AND password = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("userId");
                String role = rs.getString("role");
                String pass = rs.getString("password");
                // Karena di tabel user image_0225ee.png tidak ada kolom 'nama', 
                // kita gunakan 'username' sebagai 'nama' untuk sementara.
                String nama = rs.getString("username"); 

                User u;
                if ("ADMIN".equalsIgnoreCase(role)) {
                    u = new Admin(id, username, pass, nama);
                } else {
                    u = new Anggota(id, username, pass, nama);
                }
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getById(int id) {
        String sql = "SELECT userId, username, password, role FROM user WHERE userId = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String username = rs.getString("username");
                String pass = rs.getString("password");
                String nama = rs.getString("username");

                if ("ADMIN".equalsIgnoreCase(role)) {
                    return new Admin(id, username, pass, nama);
                } else {
                    return new Anggota(id, username, pass, nama);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}