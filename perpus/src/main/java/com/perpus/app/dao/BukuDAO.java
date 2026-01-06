package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Buku;
import com.perpus.config.Database;

public class BukuDAO {

    /* ===============================
       GET ALL BUKU
       =============================== */
    public List<Buku> getAll() {
    List<Buku> list = new ArrayList<>();
    // Gunakan JOIN untuk mengambil nama_kategori dari tabel kategori
    String sql = "SELECT b.*, k.nama_kategori " +
                 "FROM buku b " +
                 "LEFT JOIN kategori k ON b.kategoriID = k.kategoriID";

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Buku buku = new Buku(
                rs.getInt("bukuId"),
                rs.getInt("kategoriID"),
                rs.getString("judul"),
                rs.getString("penerbit"),
                rs.getInt("tahun_terbit"),
                rs.getInt("stok")
            );
            // SET NAMA KATEGORI DARI HASIL JOIN
            buku.setNamaKategori(rs.getString("nama_kategori")); 
            list.add(buku);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    /* ===============================
       INSERT BUKU
       =============================== */
    public boolean insert(Buku buku) {
    // Urutan kolom di DB: kategoriID, judul, penerbit, tahun_terbit, stok
    String sql = "INSERT INTO buku (kategoriID, judul, penerbit, tahun_terbit, stok) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // SET PARAMETER SESUAI URUTAN QUERY DI ATAS
        ps.setInt(1, buku.getKategoriId());  // Pastikan ID ini ada di tabel kategori
        ps.setString(2, buku.getJudul());
        ps.setString(3, buku.getPenerbit());
        ps.setInt(4, buku.getTahunTerbit());
        ps.setInt(5, buku.getStok());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        // Lihat pesan error ini di terminal untuk tahu alasan penolakan DB
        System.err.println("Gagal Simpan Buku: " + e.getMessage());
        return false;
    }
}

    /* ===============================
       UPDATE DATA BUKU
       =============================== */
    public boolean update(Buku buku) {
        String sql = "UPDATE buku SET kategoriID = ?, judul = ?, penerbit = ?, tahun_terbit = ?, stok = ? " +
                     "WHERE bukuId = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, buku.getKategoriId());
            ps.setString(2, buku.getJudul());
            ps.setString(3, buku.getPenerbit());
            ps.setInt(4, buku.getTahunTerbit());
            ps.setInt(5, buku.getStok());
            ps.setInt(6, buku.getBukuId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===============================
       UPDATE STOK
       =============================== */
    public boolean updateStok(int bukuId, int perubahan) {
        String sql = "UPDATE buku SET stok = stok + ? WHERE bukuId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, perubahan);
            ps.setInt(2, bukuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean kurangiStok(int bukuId, int jumlah) {
        String sql = "UPDATE buku SET stok = stok - ? WHERE bukuId = ? AND stok >= ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setInt(2, bukuId);
            ps.setInt(3, jumlah);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ===============================
       DELETE BUKU
       =============================== */
    public boolean delete(int bukuId) {
        String sql = "DELETE FROM buku WHERE bukuId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bukuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}