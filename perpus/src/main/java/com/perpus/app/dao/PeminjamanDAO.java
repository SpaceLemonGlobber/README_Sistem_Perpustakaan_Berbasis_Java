package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    String sqlHeader = "INSERT INTO peminjaman (anggotaId, adminId, tanggal_pinjam, status, denda) VALUES (?, ?, ?, ?, ?)";
    String sqlDetail = "INSERT INTO detailpeminjaman (peminjamanId, bukuId, jumlah_pinjam) VALUES (?, ?, 1)";

    try (Connection conn = Database.getConnection()) {
        conn.setAutoCommit(false); // Mulai transaksi

        try (PreparedStatement psH = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS)) {
            psH.setInt(1, p.getAnggotaId());
            psH.setObject(2, p.getAdminId() == 0 ? null : p.getAdminId()); // adminId boleh null di DB
            psH.setDate(3, Date.valueOf(p.getTanggalPeminjaman()));
            psH.setString(4, p.getStatus());
            psH.setDouble(5, p.getDenda());
            psH.executeUpdate();

            // Ambil peminjamanId yang baru saja dibuat
            ResultSet rs = psH.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                try (PreparedStatement psD = conn.prepareStatement(sqlDetail)) {
                    psD.setInt(1, newId);
                    psD.setInt(2, p.getBukuId());
                    psD.executeUpdate();
                }
            }
            conn.commit(); // Simpan permanen
            return true;
        } catch (SQLException e) {
            conn.rollback(); // Batalkan jika ada yang gagal
            e.printStackTrace();
            return false;
        }
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

public List<Peminjaman> getAll() {
    List<Peminjaman> list = new ArrayList<>();
    // Query diperbaiki sesuai struktur DB Anda (peminjaman -> detailpeminjaman -> buku)
    String sql = "SELECT p.*, a.nama as namaAnggota, b.judul as judulBuku, dp.bukuId " +
                 "FROM peminjaman p " +
                 "JOIN anggota a ON p.anggotaId = a.anggotaId " +
                 "JOIN detailpeminjaman dp ON p.peminjamanId = dp.peminjamanId " +
                 "JOIN buku b ON dp.bukuId = b.bukuId";
    
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            Peminjaman p = mapResultSetToPeminjaman(rs);
            // Ambil data dari JOIN
            p.setBukuId(rs.getInt("bukuId")); 
            p.setNamaAnggota(rs.getString("namaAnggota"));
            p.setJudulBuku(rs.getString("judulBuku"));
            list.add(p);
        }
    } catch (SQLException e) {
        System.err.println("SQL Error di getAll: " + e.getMessage());
    }
    return list;
}

// Tambahkan juga method getByStatus agar Dashboard tidak error
public List<Peminjaman> getByStatus(String status) {
    List<Peminjaman> list = new ArrayList<>();
    String sql = "SELECT p.*, a.nama as namaAnggota, b.judul as judulBuku, dp.bukuId " +
                 "FROM peminjaman p " +
                 "JOIN anggota a ON p.anggotaId = a.anggotaId " +
                 "JOIN detailpeminjaman dp ON p.peminjamanId = dp.peminjamanId " +
                 "JOIN buku b ON dp.bukuId = b.bukuId " +
                 "WHERE p.status = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Peminjaman p = mapResultSetToPeminjaman(rs);
            p.setBukuId(rs.getInt("bukuId"));
            p.setNamaAnggota(rs.getString("namaAnggota"));
            p.setJudulBuku(rs.getString("judulBuku"));
            list.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

}