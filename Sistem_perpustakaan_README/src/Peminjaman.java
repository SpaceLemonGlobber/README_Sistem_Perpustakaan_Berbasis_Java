import java.time.LocalDate;

public class Peminjaman {
    private static int counter = 1;
    private int peminjamanId;
    private LocalDate tanggalPinjam;
    private String status;
    private Buku buku;
    private Anggota anggota;

    public Peminjaman(Anggota anggota, Buku buku) {
        this.peminjamanId = counter++;
        this.tanggalPinjam = LocalDate.now();
        this.status = "aktif";
        this.anggota = anggota;
        this.buku = buku;
    }

    public void ubahStatus(String statusBaru) {
        this.status = statusBaru;
    }

    public int getPeminjamanId() { return peminjamanId; }
    public String getStatus() { return status; }
    public Buku getBuku() { return buku; }

    @Override
    public String toString() {
        return "Peminjaman #" + peminjamanId + " - " + buku.getJudul() +
                " (" + status + ", " + tanggalPinjam + ")";
    }
}
