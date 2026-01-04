package com.perpus.app.dao;

import com.perpus.app.models.Anggota;
import com.perpus.config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {

    public List<Anggota> getAll() {
        List<Anggota> list = new ArrayList<>();
        // Query JOIN untuk menggabungkan tabel user dan anggota
        String sql = "SELECT u.userId, u.username, u.password, a.anggotaId, a.nama, a.email, a.no_telp " +
                     "FROM user u JOIN anggota a ON u.userId = a.userId";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Pastikan Constructor di model Anggota sudah mendukung urutan ini
                Anggota agt = new Anggota(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("nama"),
                    rs.getInt("anggotaId"),
                    rs.getString("email"),
                    rs.getString("no_telp") // Sesuai kolom db: no_telp
                );
                list.add(agt);
            }
        } catch (SQLException e) {
            System.err.println("Error di AnggotaDAO: " + e.getMessage());
        }
        return list;
    }
}