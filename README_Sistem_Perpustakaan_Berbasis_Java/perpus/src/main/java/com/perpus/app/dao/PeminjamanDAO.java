package com.perpus.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.perpus.app.models.Peminjaman;
import com.perpus.config.Database;

public class PeminjamanDAO {

    public boolean save(Peminjaman p) {
        String sql =
            "INSERT INTO peminjaman " +
            "(anggotaId, adminId, tanggal_pinjam, tanggal_kembali, status, denda) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            )) {

            ps.setInt(1, p.getAnggotaId());
            ps.setNull(2, Types.INTEGER);
            ps.setDate(3, Date.valueOf(p.getTanggalPeminjaman()));
            ps.setDate(4, Date.valueOf(p.getTanggalPengembalian()));
            ps.setString(5, p.getStatus());
            ps.setDouble(6, p.getDenda());

            ps.executeUpdate(); 

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setPeminjamanId(rs.getInt(1));
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insert(Peminjaman p) {

    String sqlHeader = "INSERT INTO peminjaman (anggotaId, adminId, tanggal_pinjam, status, denda) VALUES (?, ?, ?, ?, ?)";
    String sqlDetail = "INSERT INTO detailpeminjaman (peminjamanId, bukuId, jumlah_pinjam) VALUES (?, ?, 1)";

    try (Connection conn = Database.getConnection()) {
        conn.setAutoCommit(false); 

        try (PreparedStatement psH = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS)) {
            psH.setInt(1, p.getAnggotaId());
            psH.setObject(2, p.getAdminId() == 0 ? null : p.getAdminId()); 
            psH.setDate(3, Date.valueOf(p.getTanggalPeminjaman()));
            psH.setString(4, p.getStatus());
            psH.setDouble(5, p.getDenda());
            psH.executeUpdate();

            ResultSet rs = psH.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                try (PreparedStatement psD = conn.prepareStatement(sqlDetail)) {
                    psD.setInt(1, newId);
                    psD.setInt(2, p.getBukuId());
                    psD.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public List<Peminjaman> getByAnggotaAndStatus(int anggotaId, String status) {
        List<Peminjaman> list = new ArrayList<>();

        String sql =
            "SELECT p.*, dp.bukuId, b.judul AS judulBuku " +
            "FROM peminjaman p " +
            "JOIN detailpeminjaman dp ON p.peminjamanId = dp.peminjamanId " +
            "JOIN buku b ON dp.bukuId = b.bukuId " +
            "WHERE p.anggotaId = ? AND p.status = ?";


        try (Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, anggotaId);
            ps.setString(2, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Peminjaman p = mapResultSetToPeminjaman(rs);
                p.setBukuId(rs.getInt("bukuId"));
                p.setJudulBuku(rs.getString("judulBuku"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


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

public int getAnggotaIdByUserId(int userId) {
    String sql = "SELECT anggotaId FROM anggota WHERE userId = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("anggotaId");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0; 
}

public void insertDetail(int peminjamanId, int bukuId, int jumlah) {
    String sql =
        "INSERT INTO detailpeminjaman (peminjamanId, bukuId, jumlah_pinjam) " +
        "VALUES (?, ?, ?)";

    try (Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, peminjamanId);
        ps.setInt(2, bukuId);
        ps.setInt(3, jumlah);

        ps.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}