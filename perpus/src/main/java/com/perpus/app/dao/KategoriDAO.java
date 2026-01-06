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