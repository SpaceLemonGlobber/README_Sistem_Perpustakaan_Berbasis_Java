import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    /* ===============================
       GET ALL BUKU
       =============================== */
    public List<Buku> getAll() {
        List<Buku> list = new ArrayList<>();
        String sql =
            "SELECT buku_id, judul, penerbit, tahun_terbit, stok " +
            "FROM buku";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Buku buku = new Buku(
                    rs.getInt("buku_id"),
                    rs.getString("judul"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("stok")
                );
                list.add(buku);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===============================
       GET BUKU BY ID
       =============================== */
    public Buku getById(int id) {
        String sql =
            "SELECT buku_id, judul, penerbit, tahun_terbit, stok " +
            "FROM buku " +
            "WHERE buku_id = ?";

        try (Connection conn = Koneksi.getConnection();
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ===============================
       INSERT BUKU
       =============================== */
    public boolean insert(Buku buku) {
        String sql =
            "INSERT INTO buku (judul, penerbit, tahun_terbit, stok) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, buku.getJudul());
            ps.setString(2, buku.getPenerbit());
            ps.setInt(3, buku.getTahunTerbit());
            ps.setInt(4, buku.getStok());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===============================
       UPDATE DATA BUKU
       =============================== */
    public boolean update(Buku buku) {
        String sql =
            "UPDATE buku SET judul = ?, penerbit = ?, tahun_terbit = ?, stok = ? " +
            "WHERE buku_id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, buku.getJudul());
            ps.setString(2, buku.getPenerbit());
            ps.setInt(3, buku.getTahunTerbit());
            ps.setInt(4, buku.getStok());
            ps.setInt(5, buku.getBukuId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===============================
       UPDATE STOK SAJA
       =============================== */
    public boolean updateStok(int bukuId, int perubahan) {
        String sql =
            "UPDATE buku SET stok = stok + ? " +
            "WHERE buku_id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, perubahan);
            ps.setInt(2, bukuId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
