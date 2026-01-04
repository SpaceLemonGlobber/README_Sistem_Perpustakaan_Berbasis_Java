package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Admin;
import com.perpus.config.Database;

public class AdminDAO {

    public List<Admin> getAll() {
        List<Admin> list = new ArrayList<>();
        // Query JOIN untuk mengambil data dari dua tabel sekaligus
        String sql = "SELECT u.userId, u.username, u.password, a.nama_admin, a.adminId, a.email " +
                     "FROM user u JOIN admin a ON u.userId = a.userId";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Mapping sesuai kolom di DB (image_012afe.png)
                Admin admin = new Admin(
                    rs.getInt("userId"),      // Dari tabel user
                    rs.getString("username"), // Dari tabel user
                    rs.getString("password"), // Dari tabel user
                    rs.getString("nama_admin"), // Dari tabel admin
                    rs.getInt("adminId"),     // Dari tabel admin
                    rs.getString("email")     // Dari tabel admin
                );
                list.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Method untuk menambah admin baru (Insert ke dua tabel)
    public boolean insert(Admin admin) {
        // Logika insert admin lebih kompleks karena harus ke tabel User dulu, 
        // baru ambil ID-nya untuk ke tabel Admin.
        return false; // Implementasikan jika dibutuhkan fitur tambah admin
    }
}