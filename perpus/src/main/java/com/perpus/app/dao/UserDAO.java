package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User login(String username, String password) {
        String sql = """
            SELECT user_id, username, password, nama, role
            FROM user
            WHERE username = ? AND password = ?
        """;

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String nama = rs.getString("nama");
                String role = rs.getString("role");

                if (role.equalsIgnoreCase(Admin.ROLE)) {
                    return new Admin(id, username, password, nama);
                }

                if (role.equalsIgnoreCase(Anggota.ROLE)) {
                    return new Anggota(id, username, password, nama);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

