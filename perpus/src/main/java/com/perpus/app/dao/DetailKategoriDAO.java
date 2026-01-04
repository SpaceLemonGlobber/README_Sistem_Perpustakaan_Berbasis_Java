package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Buku;
import com.perpus.app.models.DetailKategori;
import com.perpus.app.models.Kategori;
import com.perpus.config.Database;

public class DetailKategoriDAO {

    public List<DetailKategori> getAll() {
        List<DetailKategori> list = new ArrayList<>();
        // Query JOIN 3 tabel untuk mengambil info lengkap sesuai Model kamu
        String sql = "SELECT dk.kategoriID, k.nama_kategori, k.deskripsi, " +
                     "dk.bukuID, b.judul, b.penerbit, b.tahun_terbit, b.stok " +
                     "FROM detailkategori dk " +
                     "JOIN kategori k ON dk.kategoriID = k.kategoriID " +
                     "JOIN buku b ON dk.bukuID = b.bukuId";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // 1. Buat objek Kategori dari hasil database
                Kategori kategori = new Kategori(
                    rs.getInt("kategoriID"),
                    rs.getString("nama_kategori"),
                    rs.getString("deskripsi")
                );

                // 2. Buat objek Buku (sesuai constructor Buku yang kita fix tadi)
                // Karena kolom di tabel detailkategori adalah bukuID (D kapital), gunakan itu
                Buku buku = new Buku(
                    rs.getInt("bukuID"), 
                    rs.getInt("kategoriID"),
                    rs.getString("judul"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("stok")
                );

                // 3. Masukkan ke Model DetailKategori
                list.add(new DetailKategori(kategori, buku));
            }
        } catch (SQLException e) {
            System.err.println("Error di DetailKategoriDAO: " + e.getMessage());
        }
        return list;
    }
}