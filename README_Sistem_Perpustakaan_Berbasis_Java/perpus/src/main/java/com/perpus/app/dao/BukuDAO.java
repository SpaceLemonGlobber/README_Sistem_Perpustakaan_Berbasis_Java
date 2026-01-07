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

    public List<Buku> getAll() {
    List<Buku> list = new ArrayList<>();
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
            buku.setNamaKategori(rs.getString("nama_kategori")); 
            list.add(buku);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    public boolean insert(Buku buku) {
    String sql = "INSERT INTO buku (kategoriID, judul, penerbit, tahun_terbit, stok) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, buku.getKategoriId()); 
        ps.setString(2, buku.getJudul());
        ps.setString(3, buku.getPenerbit());
        ps.setInt(4, buku.getTahunTerbit());
        ps.setInt(5, buku.getStok());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Gagal Simpan Buku: " + e.getMessage());
        return false;
    }
}

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

    public Buku getById(int id) {
    String sql = "SELECT * FROM buku WHERE bukuId = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Buku buku = new Buku(
                rs.getInt("bukuId"),
                rs.getInt("kategoriID"),
                rs.getString("judul"),
                rs.getString("penerbit"),
                rs.getInt("tahun_terbit"),
                rs.getInt("stok")
            );
            return buku;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}