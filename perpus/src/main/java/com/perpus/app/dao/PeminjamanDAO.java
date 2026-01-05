package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Peminjaman;
import com.perpus.config.Database;

public class PeminjamanDAO {

    // --- 1. Method SAVE (Alias untuk Insert agar Controller tidak error) ---
    public boolean save(Peminjaman p) {
        return insert(p);
    }

    // --- 2. Method INSERT (Disesuaikan dengan field model Anda) ---
    public boolean insert(Peminjaman p) {
        String sql = "INSERT INTO peminjaman (anggotaId, adminId, bukuId, tanggal_pinjam, tanggal_kembali, status, denda) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getAnggotaId());
            ps.setInt(2, p.getAdminId()); // Pastikan adminId diisi (bisa 0 jika member yang pinjam mandiri)
            ps.setInt(3, p.getBukuId());
            ps.setDate(4, Date.valueOf(p.getTanggalPeminjaman()));
            // Tambahkan tanggal kembali agar tidak error compilation lagi
            ps.setDate(5, p.getTanggalPengembalian() != null ? Date.valueOf(p.getTanggalPengembalian()) : null);
            ps.setString(6, p.getStatus());
            ps.setDouble(7, p.getDenda());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- 3. Method GET BY ANGGOTA & STATUS (Dibutuhkan UserDashboardController) ---
    public List<Peminjaman> getByAnggotaAndStatus(int anggotaId, String status) {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT p.*, b.judul FROM peminjaman p " +
                     "JOIN buku b ON p.bukuId = b.bukuId " +
                     "WHERE p.anggotaId = ? AND p.status = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, anggotaId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Peminjaman p = mapResultSetToPeminjaman(rs);
                // Set judul buku agar muncul di TableView User
                p.setJudulBuku(rs.getString("judul")); 
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ==========================================================
       METHOD LAINNYA (TETAP SAMA NAMUN DENGAN PENYEMPURNAAN)
       ========================================================== */

    public Peminjaman getById(int id) {
        String sql = "SELECT * FROM peminjaman WHERE peminjamanId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToPeminjaman(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Peminjaman> getByAnggota(int anggotaId) {
        List<Peminjaman> list = new ArrayList<>();
        // Join buku untuk mendapatkan judul di riwayat
        String sql = "SELECT p.*, b.judul FROM peminjaman p JOIN buku b ON p.bukuId = b.bukuId WHERE p.anggotaId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, anggotaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Peminjaman p = mapResultSetToPeminjaman(rs);
                p.setJudulBuku(rs.getString("judul"));
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean updateStatus(int peminjamanId, String status) {
        String sql = "UPDATE peminjaman SET status = ? WHERE peminjamanId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, peminjamanId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Peminjaman mapResultSetToPeminjaman(ResultSet rs) throws SQLException {
        Peminjaman p = new Peminjaman();
        p.setPeminjamanId(rs.getInt("peminjamanId"));
        p.setAnggotaId(rs.getInt("anggotaId"));
        p.setAdminId(rs.getInt("adminId"));
        p.setBukuId(rs.getInt("bukuId"));
        
        if (rs.getDate("tanggal_pinjam") != null) {
            p.setTanggalPeminjaman(rs.getDate("tanggal_pinjam").toLocalDate());
        }
        if (rs.getDate("tanggal_kembali") != null) {
            p.setTanggalPengembalian(rs.getDate("tanggal_kembali").toLocalDate());
        }
        
        p.setStatus(rs.getString("status"));
        p.setDenda(rs.getDouble("denda"));
        return p;
    }
    // Tambahkan method ini di com.perpus.app.dao.PeminjamanDAO.java

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
}