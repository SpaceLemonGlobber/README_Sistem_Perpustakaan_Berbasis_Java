package com.perpus.app.dao;

import com.perpus.app.models.Peminjaman;
import com.perpus.config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanDAO {

    /* ==========================================================
       1. GET BY ID: Mencari satu data pinjaman berdasarkan ID
       ========================================================== */
    public Peminjaman getById(int id) {
        String sql = "SELECT * FROM peminjaman WHERE peminjamanId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToPeminjaman(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ==========================================================
       2. GET BY ANGGOTA: Melihat riwayat pinjam satu anggota tertentu
       ========================================================== */
    public List<Peminjaman> getByAnggota(int anggotaId) {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman WHERE anggotaId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, anggotaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToPeminjaman(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ==========================================================
       3. GET BY STATUS: Memfilter (misal: hanya yang DIPINJAM)
       ========================================================== */
    public List<Peminjaman> getByStatus(String status) {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman WHERE status = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToPeminjaman(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ==========================================================
       4. UPDATE STATUS: Merubah status (misal dari DIPINJAM ke DIKEMBALIKAN)
       ========================================================== */
    public boolean updateStatus(int peminjamanId, String status) {
        String sql = "UPDATE peminjaman SET status = ? WHERE peminjamanId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, peminjamanId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ==========================================================
       5. GET ALL: Mengambil semua data peminjaman
       ========================================================== */
    public List<Peminjaman> getAll() {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToPeminjaman(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ==========================================================
       6. INSERT: Tambah data peminjaman baru
       ========================================================== */
    public boolean insert(Peminjaman p) {
        String sql = "INSERT INTO peminjaman (anggotaId, adminId, bukuId, tanggal_pinjam, status, denda) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getAnggotaId());
            ps.setInt(2, p.getAdminId());
            ps.setInt(3, p.getBukuId());
            ps.setDate(4, Date.valueOf(p.getTanggalPinjam()));
            ps.setString(5, p.getStatus());
            ps.setDouble(6, p.getDenda());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ==========================================================
       HELPER METHOD: Agar tidak menulis ulang pembuatan objek
       ========================================================== */
    private Peminjaman mapResultSetToPeminjaman(ResultSet rs) throws SQLException {
        return new Peminjaman(
            rs.getInt("peminjamanId"),
            rs.getInt("anggotaId"),
            rs.getInt("adminId"),
            rs.getInt("bukuId"),
            rs.getDate("tanggal_pinjam").toLocalDate(),
            rs.getDate("tanggal_kembali") != null ? rs.getDate("tanggal_kembali").toLocalDate() : null,
            rs.getString("status"),
            rs.getDouble("denda")
        );
    }
}