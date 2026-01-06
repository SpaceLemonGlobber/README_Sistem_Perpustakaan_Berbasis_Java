package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Anggota;
import com.perpus.config.Database;

public class AnggotaDAO {

    public List<Anggota> getAll() {
        List<Anggota> list = new ArrayList<>();
        // Query JOIN sesuai screenshot database Anda
        String sql = "SELECT u.userId, u.username, u.password, a.anggotaId, a.nama, a.email, a.no_telp " +
                     "FROM user u JOIN anggota a ON u.userId = a.userId";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Pastikan Constructor di model Anggota urutannya sesuai ini
                Anggota agt = new Anggota(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("nama"),
                    rs.getInt("anggotaId"),
                    rs.getString("email"),
                    rs.getString("no_telp")
                );
                list.add(agt);
            }
        } catch (SQLException e) {
            System.err.println("Error di AnggotaDAO (getAll): " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Anggota a) {
        String sqlUser = "INSERT INTO user (username, password, role) VALUES (?, ?, 'anggota')";
        String sqlAnggota = "INSERT INTO anggota (userId, nama, email, no_telp) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false); // Mulai Transaksi

            // 1. Simpan ke tabel user
            PreparedStatement psUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, a.getUsername());
            psUser.setString(2, a.getPassword());
            psUser.executeUpdate();

            // Ambil ID yang baru saja digenerate
            ResultSet rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                int generatedUserId = rs.getInt(1);

                // 2. Simpan ke tabel anggota menggunakan generatedUserId
                PreparedStatement psAnggota = conn.prepareStatement(sqlAnggota);
                psAnggota.setInt(1, generatedUserId);
                psAnggota.setString(2, a.getNama());
                psAnggota.setString(3, a.getEmail());
                psAnggota.setString(4, a.getNo_telp());
                psAnggota.executeUpdate();
            }

            conn.commit(); // Simpan permanen
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Error Insert Anggota: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Anggota a) {
        String sqlUser = "UPDATE user SET username = ?, password = ? WHERE userId = ?";
        String sqlAnggota = "UPDATE anggota SET nama = ?, email = ?, no_telp = ? WHERE anggotaId = ?";

        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // Update tabel user
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, a.getUsername());
            psUser.setString(2, a.getPassword());
            psUser.setInt(3, a.getUserId());
            psUser.executeUpdate();

            // Update tabel anggota
            PreparedStatement psAnggota = conn.prepareStatement(sqlAnggota);
            psAnggota.setString(1, a.getNama());
            psAnggota.setString(2, a.getEmail());
            psAnggota.setString(3, a.getNo_telp());
            psAnggota.setInt(4, a.getAnggotaId());
            psAnggota.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        }
    }

    public boolean delete(int userId) {
        String sql = "DELETE FROM user WHERE userId = ?";
    
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, userId);
        return ps.executeUpdate() > 0;
        
    } catch (SQLException e) {
        System.err.println("Gagal menghapus user: " + e.getMessage());
        return false;
    }

}

}