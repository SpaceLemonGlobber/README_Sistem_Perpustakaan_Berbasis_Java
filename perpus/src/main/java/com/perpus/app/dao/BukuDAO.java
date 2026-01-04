package com.perpus.app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    /* =========================
       GET SEMUA BUKU
       ========================= */
    public List<Buku> getAll() {
        String sql = """
            SELECT buku_id, judul, penerbit, tahun_terbit, stok
            FROM buku
            ORDER BY judul
        """;

        List<Buku> list = new ArrayList<>();

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Buku b = new Buku(
                        rs.getInt("buku_id"),
                        rs.getString("judul"),
                        rs.getString("penerbit"),
                        rs.getInt("tahun_terbit"),
                        rs.getInt("stok")
                );
                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* =========================
       GET BY ID
       ========================= */
    public Buku getById(int id) {
        String sql = """
            SELECT buku_id, judul, penerbit, tahun_terbit, stok
            FROM buku
            WHERE buku_id = ?
        """;

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Buku(
                        rs.getInt("buku_id"),
                        rs.getString("judul"),
                        rs.getString("penerbit"),
                        rs.getInt("tahun_terbit"),
                        rs.getInt("stok")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* =========================
       INSERT BUKU
       ========================= */
    public boolean insert(Buku b) {
        String sql = """
            INSERT INTO buku (judul, penerbit, tahun_terbit, stok)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getJudul());
            ps.setString(2, b.getPenerbit());
            ps.setInt(3, b.getTahunTerbit());
            ps.setInt(4, b.getStok());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =========================
       UPDATE BUKU
       ========================= */
    public boolean update(Buku b) {
        String sql = """
            UPDATE buku
            SET judul = ?, penerbit = ?, tahun_terbit = ?, stok = ?
            WHERE buku_id = ?
        """;

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getJudul());
            ps.setString(2, b.getPenerbit());
            ps.setInt(3, b.getTahunTerbit());
            ps.setInt(4, b.getStok());
            ps.setInt(5, b.getBukuId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =========================
       UPDATE STOK (dipakai peminjaman)
       ========================= */
    public boolean updateStok(int bukuId, int perubahan) {
        String sql = """
            UPDATE buku
            SET stok = stok + ?
            WHERE buku_id = ?
        """;

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, perubahan);
            ps.setInt(2, bukuId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =========================
       DELETE BUKU
       ========================= */
    public boolean delete(int bukuId) {
        String sql = "DELETE FROM buku WHERE buku_id = ?";

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bukuId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

