package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Anggota;
import com.perpus.config.Database;

public class AnggotaDAO {

    public List<Anggota> getAll() {
        List<Anggota> list = new ArrayList<>();
        String sql = "SELECT u.userId, u.username, u.password, a.anggotaId, a.nama, a.email, a.no_telp " +
                     "FROM user u JOIN anggota a ON u.userId = a.userId";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
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

    /* ==========================================================
       TAMBAHAN: Method DELETE (Menghilangkan Error di Controller)
       ========================================================== */
    public boolean delete(int id) {
        // Karena kita menggunakan tabel user dan anggota, kita harus hapus di tabel anggota dulu (FK)
        // Note: 'id' di sini diasumsikan adalah anggotaId atau userId sesuai mapping model
        String sql = "DELETE FROM anggota WHERE anggotaId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error di AnggotaDAO (delete): " + e.getMessage());
            return false;
        }
    }

    public boolean insert(Anggota a) {
        // Logika insert ke tabel user dulu, ambil generateId, lalu insert ke tabel anggota
        return false; // Implementasi sesuai kebutuhan db
    }

    public Integer getAnggotaIdByUserId(int userId) {
        String sql = "SELECT anggotaId FROM anggota WHERE userID = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("anggotaId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}