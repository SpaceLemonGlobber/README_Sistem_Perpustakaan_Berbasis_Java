import java.util.*;

public class Anggota extends User {
    private String nama;
    private String email;
    private String noTelp;
    private List<Peminjaman> riwayat = new ArrayList<>();

    public Anggota(int id, String nama, String email, String noTelp, String username, String password) {
        super(id, username, password, "anggota");
        this.nama = nama;
        this.email = email;
        this.noTelp = noTelp;
    }

    @Override
    public void menu() {
        System.out.println("Halo " + nama + ", selamat datang di perpustakaan!");
    }

    public void pinjamBuku(Buku b) {
        if (b.getStok() > 0) {
            Peminjaman p = new Peminjaman(this, b);
            riwayat.add(p);
            b.updateStok(-1);
            System.out.println("Berhasil meminjam: " + b.getJudul());
        } else {
            System.out.println("Buku tidak tersedia!");
        }
    }

    public void kembalikanBuku(int idPeminjaman) {
        for (Peminjaman p : riwayat) {
            if (p.getPeminjamanId() == idPeminjaman && p.getStatus().equals("aktif")) {
                p.ubahStatus("selesai");
                p.getBuku().updateStok(1);
                System.out.println("Buku telah dikembalikan.");
                return;
            }
        }
        System.out.println("Peminjaman tidak ditemukan.");
    }

    public void lihatRiwayat() {
        if (riwayat.isEmpty()) {
            System.out.println("Belum ada riwayat peminjaman.");
            return;
        }
        riwayat.forEach(System.out::println);
    }
}

