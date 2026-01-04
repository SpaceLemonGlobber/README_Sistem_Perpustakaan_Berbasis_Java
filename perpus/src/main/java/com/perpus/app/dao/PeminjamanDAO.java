package com.perpus.app.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Anggota;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Peminjaman;

public class PeminjamanDAO {

    /* ===============================
       TAMBAH PEMINJAMAN
       =============================== */
    public boolean insert(Peminjaman p) {
        String sqlPeminjaman = """
            INSERT INTO peminjaman (anggota_id, tanggal_pinjam, status)
            VALUES (?, ?, ?)
        """;

        String sqlDetail = """
            INSERT INTO detail_peminjaman (peminjaman_id, buku_id)
            VALUES (?, ?)
        """;

        try (Connection conn = koneksi.getConnection()) {
            conn.setAutoCommit(false);

            // insert peminjaman
            PreparedStatement ps = conn.prepareStatement(
                    sqlPeminjaman, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getAnggota().getUserId());
            ps.setDate(2, Date.valueOf(p.getTanggalPinjam()));
            ps.setString(3, p.getStatus());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) return false;

            int peminjamanId = rs.getInt(1);

            // insert detail
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setInt(1, peminjamanId);
            psDetail.setInt(2, p.getBuku().getBukuId());
            psDetail.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===============================
       UPDATE STATUS
       =============================== */
    public boolean updateStatus(int peminjamanId, String status) {
        String sql = "UPDATE peminjaman SET status = ? WHERE peminjaman_id = ?";

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, peminjamanId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===============================
       GET PEMINJAMAN AKTIF
       =============================== */
    public List<Peminjaman> getAktif() {
        String sql = """
            SELECT p.peminjaman_id, p.tanggal_pinjam, p.status,
                   b.buku_id, b.judul,
                   u.user_id, u.username, u.password, u.nama
            FROM peminjaman p
            JOIN detail_peminjaman dp ON p.peminjaman_id = dp.peminjaman_id
            JOIN buku b ON dp.buku_id = b.buku_id
            JOIN user u ON p.anggota_id = u.user_id
            WHERE p.status = 'aktif'
        """;

        List<Peminjaman> list = new ArrayList<>();

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Anggota anggota = new Anggota(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nama")
                );

                Buku buku = new Buku(
                        rs.getInt("buku_id"),
                        rs.getString("judul"),
                        null, 0, 0
                );

                Peminjaman p = new Peminjaman(anggota, buku);
                p.setPeminjamanId(rs.getInt("peminjaman_id"));
                p.ubahStatus(rs.getString("status"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===============================
       GET RIWAYAT ANGGOTA
       =============================== */
    public List<Peminjaman> getByAnggota(int anggotaId) {
        String sql = """
            SELECT p.peminjaman_id, p.tanggal_pinjam, p.status,
                   b.buku_id, b.judul
            FROM peminjaman p
            JOIN detail_peminjaman dp ON p.peminjaman_id = dp.peminjaman_id
            JOIN buku b ON dp.buku_id = b.buku_id
            WHERE p.anggota_id = ?
        """;

        List<Peminjaman> list = new ArrayList<>();

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, anggotaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Buku buku = new Buku(
                        rs.getInt("buku_id"),
                        rs.getString("judul"),
                        null, 0, 0
                );

                Peminjaman p = new Peminjaman(null, buku);
                p.setPeminjamanId(rs.getInt("peminjaman_id"));
                p.ubahStatus(rs.getString("status"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Peminjaman getById(int peminjamanId) {
        String sql = """
            SELECT p.peminjaman_id, p.status,
                b.buku_id, b.judul
            FROM peminjaman p
            JOIN detail_peminjaman dp ON p.peminjaman_id = dp.peminjaman_id
            JOIN buku b ON dp.buku_id = b.buku_id
            WHERE p.peminjaman_id = ?
        """;

        try (Connection conn = koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, peminjamanId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Buku buku = new Buku(
                        rs.getInt("buku_id"),
                        rs.getString("judul"),
                        null, 0, 0
                );

                Peminjaman p = new Peminjaman(null, buku);
                p.setPeminjamanId(peminjamanId);
                p.ubahStatus(rs.getString("status"));
                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

