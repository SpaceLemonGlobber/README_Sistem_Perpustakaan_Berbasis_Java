package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Kategori;
import com.perpus.config.Database;

public class KategoriDAO {

    public boolean save(Kategori kategori) {
        String sql = "INSERT INTO kategori (nama_kategori, deskripsi) VALUES (?, ?)";
        try (java.sql.Connection conn = com.perpus.config.Database.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kategori.getNama_kategori());
            ps.setString(2, kategori.getDeskripsi());
            
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            System.err.println("Error save kategori: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM kategori WHERE kategoriID = ?";
        try (Connection conn = com.perpus.config.Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error hapus kategori: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Kategori kategori) {
        String sql = "UPDATE kategori SET nama_kategori = ?, deskripsi = ? WHERE kategoriID = ?";
        try (Connection conn = com.perpus.config.Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kategori.getNama_kategori());
            ps.setString(2, kategori.getDeskripsi());
            ps.setInt(3, kategori.getKategoriId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update kategori: " + e.getMessage());
            return false;
        }
    }


    public List<Kategori> getAll() {
        List<Kategori> list = new ArrayList<>();
        // Query langsung ke tabel master kategori
        String sql = "SELECT kategoriID, nama_kategori, deskripsi FROM kategori";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Kategori(
                    rs.getInt("kategoriID"),
                    rs.getString("nama_kategori"),
                    rs.getString("deskripsi")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error di KategoriDAO: " + e.getMessage());
        }
        return list;
    }
}