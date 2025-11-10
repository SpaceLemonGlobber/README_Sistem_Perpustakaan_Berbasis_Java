import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {
    private String nama;

    public Admin(String nama) {
        super(0, "admin", "#12345", "admin");
        this.nama = nama;
    }

    @Override
    public void menu() {
        System.out.println("Halo, " + nama + ". Anda login sebagai Admin.");
        System.out.println("==========================================");
        System.out.println("1. Tambah Buku");
        System.out.println("2. Ubah Buku");
        System.out.println("3. Hapus Buku");
        System.out.println("4. Lihat Daftar Buku");
        System.out.println("5. Logout");
        System.out.println("==========================================");
    }

    /** Tambah buku baru ke database */
    public void tambahBuku(String judul, String penerbit, int tahun, int stok) {
        String sql = "INSERT INTO buku (judul, penerbit, tahun_terbit, stok) VALUES (?, ?, ?, ?)";
        try (Connection c = koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (judul.isEmpty() || penerbit.isEmpty()) {
                System.out.println("Judul dan penerbit tidak boleh kosong!");
                return;
            }

            ps.setString(1, judul);
            ps.setString(2, penerbit);
            ps.setInt(3, tahun);
            ps.setInt(4, stok);
            ps.executeUpdate();
            System.out.println("Buku \"" + judul + "\" berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Gagal menambah buku: " + e.getMessage());
        }
    }

    /** Ubah data buku berdasarkan ID */
    public void ubahBuku(int id, String judulBaru, String penerbitBaru, int tahunBaru, int stokBaru) {
        String sql = "UPDATE buku SET judul = ?, penerbit = ?, tahun_terbit = ?, stok = ? WHERE bukuID = ?";
        try (Connection c = koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, judulBaru);
            ps.setString(2, penerbitBaru);
            ps.setInt(3, tahunBaru);
            ps.setInt(4, stokBaru);
            ps.setInt(5, id);

            int row = ps.executeUpdate();
            if (row > 0) {
                System.out.println("Data buku berhasil diubah!");
            } else {
                System.out.println("Buku dengan ID " + id + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengubah buku: " + e.getMessage());
        }
    }

    /** Hapus buku berdasarkan ID */
    public void hapusBuku(int id) {
        String sql = "DELETE FROM buku WHERE bukuID = ?";
        try (Connection c = koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            int row = ps.executeUpdate();

            if (row > 0) {
                System.out.println(" Buku berhasil dihapus!");
            } else {
                System.out.println("Buku dengan ID " + id + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghapus buku: " + e.getMessage());
        }
    }

    /** Menampilkan seluruh daftar buku */
    public void lihatBuku() {
        String sql = "SELECT * FROM buku ORDER BY bukuID ASC";
        try (Connection c = koneksi.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            System.out.println("\n=== DAFTAR BUKU ===");
            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                System.out.printf(
                    "%d. %s | %s | Tahun: %d | Stok: %d%n",
                    rs.getInt("bukuID"),
                    rs.getString("judul"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("stok")
                );
            }

            if (!adaData) {
                System.out.println("Belum ada buku di database.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data buku: " + e.getMessage());
        }
    }

    /** Mengambil daftar buku sebagai List<Buku> */
    public List<Buku> getDaftarBuku() {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT * FROM buku ORDER BY bukuID ASC";

        try (Connection c = koneksi.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Buku(
                        rs.getInt("bukuID"),
                        rs.getString("judul"),
                        rs.getString("penerbit"),
                        rs.getInt("tahun_terbit"),
                        rs.getInt("stok")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil daftar buku: " + e.getMessage());
        }
        return list;
    }
}
