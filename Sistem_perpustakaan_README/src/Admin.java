import java.util.*;

public class Admin extends User {
    private String namaAdmin;
    private List<Buku> daftarBuku = new ArrayList<>();

    public Admin(String namaAdmin) {
        super(0, "admin", "#12345", "admin");
        this.namaAdmin = namaAdmin;
    }

    @Override
    public void menu() {
        System.out.println("Selamat datang Admin: " + namaAdmin);
    }

    // Overloading (dua versi tambahBuku)
    public void tambahBuku(Buku b) {
        daftarBuku.add(b);
        System.out.println("Buku \"" + b.getJudul() + "\" berhasil ditambahkan.");
    }

    public void tambahBuku(int id, String judul, String penerbit, int tahun, int stok) {
        daftarBuku.add(new Buku(id, judul, penerbit, tahun, stok));
        System.out.println("Buku \"" + judul + "\" berhasil ditambahkan.");
    }

    public void ubahBuku(int id, String judulBaru) {
        for (Buku b : daftarBuku) {
            if (b.getBukuId() == id) {
                b.setJudul(judulBaru);
                System.out.println("Judul buku diperbarui menjadi " + judulBaru);
                return;
            }
        }
        System.out.println("Buku tidak ditemukan.");
    }

    public void hapusBuku(int id) {
        boolean removed = daftarBuku.removeIf(b -> b.getBukuId() == id);
        System.out.println(removed ? "Buku berhasil dihapus." : "Buku tidak ditemukan.");
    }

    public void lihatBuku() {
        if (daftarBuku.isEmpty()) {
            System.out.println("Belum ada buku di perpustakaan.");
            return;
        }
        System.out.println("=== DAFTAR BUKU ===");
        daftarBuku.forEach(System.out::println);
    }

    public List<Buku> getDaftarBuku() {
        return daftarBuku;
    }
}
