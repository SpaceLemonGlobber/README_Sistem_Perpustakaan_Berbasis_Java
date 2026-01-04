import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanDAO {

    /* ===============================
       INSERT PEMINJAMAN
       =============================== */
    public boolean insert(Peminjaman p) {
        String sql =
            "INSERT INTO peminjaman (anggota_id, buku_id, tanggal_pinjam, status) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getAnggota().getUserId());
            ps.setInt(2, p.getBuku().getBukuId());
            ps.setDate(3, Date.valueOf(p.getTanggalPinjam()));
            ps.setString(4, p.getStatus().name());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===============================
       GET PEMINJAMAN BY ID
       =============================== */
    public Peminjaman getById(int id) {
        String sql =
            "SELECT p.peminjaman_id, p.tanggal_pinjam, p.status, " +
            "b.buku_id, b.judul, b.penerbit, b.tahun_terbit, b.stok, " +
            "u.user_id, u.username " +
            "FROM peminjaman p " +
            "JOIN buku b ON p.buku_id = b.buku_id " +
            "JOIN user u ON p.anggota_id = u.user_id " +
            "WHERE p.peminjaman_id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Buku buku = new Buku(
                    rs.getInt("buku_id"),
                    rs.getString("judul"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("stok")
                );

                Anggota anggota = new Anggota(
                    rs.getInt("user_id"),
                    rs.getString("username")
                );

                Peminjaman p = new Peminjaman(anggota, buku);
                p.setPeminjamanId(rs.getInt("peminjaman_id"));
                p.setTanggalPinjam(rs.getDate("tanggal_pinjam").toLocalDate());
                p.setStatus(StatusPeminjaman.valueOf(rs.getString("status")));

                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ===============================
       UPDATE STATUS PEMINJAMAN
       =============================== */
    public boolean updateStatus(int peminjamanId, String status) {
        String sql =
            "UPDATE peminjaman SET status = ? " +
            "WHERE peminjaman_id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, peminjamanId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===============================
       GET PEMINJAMAN AKTIF
       =============================== */
    public List<Peminjaman> getAktif() {
        return getByStatus(StatusPeminjaman.AKTIF);
    }

    /* ===============================
       GET PEMINJAMAN BY STATUS
       =============================== */
    private List<Peminjaman> getByStatus(StatusPeminjaman status) {
        List<Peminjaman> list = new ArrayList<>();

        String sql =
            "SELECT p.peminjaman_id, p.tanggal_pinjam, p.status, " +
            "b.buku_id, b.judul, b.penerbit, b.tahun_terbit, b.stok, " +
            "u.user_id, u.username " +
            "FROM peminjaman p " +
            "JOIN buku b ON p.buku_id = b.buku_id " +
            "JOIN user u ON p.anggota_id = u.user_id " +
            "WHERE p.status = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Buku buku = new Buku(
                    rs.getInt("buku_id"),
                    rs.getString("judul"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("stok")
                );

                Anggota anggota = new Anggota(
                    rs.getInt("user_id"),
                    rs.getString("username")
                );

                Peminjaman p = new Peminjaman(anggota, buku);
                p.setPeminjamanId(rs.getInt("peminjaman_id"));
                p.setTanggalPinjam(rs.getDate("tanggal_pinjam").toLocalDate());
                p.setStatus(StatusPeminjaman.valueOf(rs.getString("status")));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===============================
       GET PEMINJAMAN BY ANGGOTA
       =============================== */
    public List<Peminjaman> getByAnggota(int anggotaId) {
        List<Peminjaman> list = new ArrayList<>();

        String sql =
            "SELECT p.peminjaman_id, p.tanggal_pinjam, p.status, " +
            "b.buku_id, b.judul, b.penerbit, b.tahun_terbit, b.stok " +
            "FROM peminjaman p " +
            "JOIN buku b ON p.buku_id = b.buku_id " +
            "WHERE p.anggota_id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, anggotaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Buku buku = new Buku(
                    rs.getInt("buku_id"),
                    rs.getString("judul"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("stok")
                );

                Anggota anggota = new Anggota(anggotaId, "");

                Peminjaman p = new Peminjaman(anggota, buku);
                p.setPeminjamanId(rs.getInt("peminjaman_id"));
                p.setTanggalPinjam(rs.getDate("tanggal_pinjam").toLocalDate());
                p.setStatus(StatusPeminjaman.valueOf(rs.getString("status")));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
